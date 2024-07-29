package com.example.agrotradehub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agrotradehub.adapters.ListViewAdapterCar;
import com.example.agrotradehub.databinding.ActivityMainBinding;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.PermisosDoc;
import com.example.agrotradehub.models.Productos;
import com.example.agrotradehub.observers.AppLifecycleObserver;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.material.badge.BadgeDrawable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int CODE_GPS = 1;
    private static final String PREFS_NAME = "MyCarProducts";
    private static final String PRODUCT_LIST_KEY = "carProductList";
    private static final String PREFS_NAME1 = "MyPrefsFile";
    private static final String PRODUCT_LIST_KEY1 = "productList";
    private static final String PREFS_URL = "URLWS";
    private Handler inactivityHandler = new Handler();
    private Runnable inactivityRunnable;
    private BadgeDrawable badgeDrawable;
    InicioFragment inicioFragment = new InicioFragment();
    SearchFragment searchFragment = new SearchFragment();
    AddFragment addFragment = new AddFragment();
    CarFragment carFragment = new CarFragment();
    ArrayList<Productos> carritoProductos;
    Switch moneda, conExistencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AgroTradeHub);
        super.onCreate(savedInstanceState);
        registerAppLifecycleObserver();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatosGlobales objGlobal = (DatosGlobales) this.getApplicationContext();
        carritoProductos = retrieveProductList();
        if (obtenerValorDesdeSharedPreferences() == "Sin valor guardado"){
            mostrarDialogoOpciones();
        }else {
            objGlobal.setEntorno(obtenerValorDesdeSharedPreferences());
            new checkServerStatus().execute(objGlobal.getEntorno());
        }
        if (objGlobal.getPermisosUsr() != null) {
            inactivityRunnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Tiempo de inactividad superado", Toast.LENGTH_LONG).show();
                    objGlobal.setPermisosUsr(null);
                    objGlobal.setCliente(null);
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    finish();
                    startActivity(i);
                }
            };
        }
        if (objGlobal.getCliente() == null && carritoProductos != null) {
            // Si los datos están activos, realiza alguna acción
            // Por ejemplo, muestra un mensaje o inicia una tarea
            for (Productos produtc: carritoProductos) {
                precioProductos(0, produtc, new PrecioProductosCallback() {
                    @Override
                    public void onPrecioProductosResponse(double precio) {
                        if (produtc.getPrecioSelect() <= 0){
                            produtc.setPrecioSelect(precio);
                            saveProductListCar(carritoProductos);
                        }
                    }

                    @Override
                    public void onPrecioProductosError(String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTopDialog(MainActivity.this);
            }
        });
        binding.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (objGlobal.getPermisosUsr() != null){
                    showBottomDialog();
                }else {
                    abrirInicioSesion();
                }
            }
        });
        if (carritoProductos != null && !carritoProductos.isEmpty()) {
            badgeDrawable = binding.bottomNavigationView.getOrCreateBadge(R.id.menuCar);
            badgeDrawable.setVisible(true);
        } else {
            badgeDrawable = binding.bottomNavigationView.getOrCreateBadge(R.id.menuCar);
            badgeDrawable.setVisible(false);
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            setMyImageButtonVisibility(View.VISIBLE);
            if (item.getItemId() == R.id.menuHome) {
                loadFragment(inicioFragment);
            } else if (item.getItemId() == R.id.menuSearch) {
                loadFragment(searchFragment);
            } else if (item.getItemId() == R.id.menuAddClient) {
                loadFragment(addFragment);
            } else if (item.getItemId() == R.id.menuCar) {
                loadFragment(carFragment);
            }
            return true;
        });
    }

    private void mostrarDialogoOpciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese la dirección del Web Service:");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(25, 0, 25, 0);
        input.setLayoutParams(params);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            // Obtener el valor ingresado por el usuario
            String valorIngresado = input.getText().toString();

            // Guardar el valor en SharedPreferences
            guardarValorEnSharedPreferences(valorIngresado);
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogAnimation;

        dialog.show();
    }

    private void guardarValorEnSharedPreferences(String valor) {
        SharedPreferences preferences = getSharedPreferences(PREFS_URL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("clave_valor", valor);
        editor.apply();
        DatosGlobales objGlobal = (DatosGlobales) this.getApplicationContext();
        new checkServerStatus().execute(valor);
        objGlobal.setEntorno(valor);
    }
    private String obtenerValorDesdeSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFS_URL, Context.MODE_PRIVATE);
        // El segundo parámetro de getString es el valor predeterminado si no se encuentra la clave
        return preferences.getString("clave_valor", "Sin valor guardado");
    }
    public void setMyImageButtonVisibility(int visibility) {
        binding.settings.setVisibility(visibility);
    }
    private void resetInactivityTimer() {
        inactivityHandler.removeCallbacks(inactivityRunnable);
        inactivityHandler.postDelayed(inactivityRunnable, 60000); // minuto y medio de inactividad
    }
    private void registerAppLifecycleObserver() {
        AppLifecycleObserver appLifecycleObserver = new AppLifecycleObserver(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
    }
    private void abrirInicioSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Inicio de sesión necesario");
        builder.setMessage("Debes iniciar sesión para continuar").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Se cancelo el inicio de sesión", Toast.LENGTH_LONG).show();
                dialogInterface.dismiss();
            }
        }).show();

    }
    @Override
    public void onUserInteraction() {
        resetInactivityTimer();
    }
    public void updateBadgeVisibility(boolean isVisible) {
        if (isVisible) {
            badgeDrawable.setVisible(true);
        } else {
            badgeDrawable.setVisible(false);
        }
    }
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
    private void showBottomDialog() {
        Context context = this;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout cotizacionesLayout = dialog.findViewById(R.id.layoutCotizaciones);
        LinearLayout pedidosLayout = dialog.findViewById(R.id.layoutPedidos);
        LinearLayout remisionesLayout = dialog.findViewById(R.id.latyutRemisiones);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        cotizacionesLayout.setVisibility(View.GONE);
        pedidosLayout.setVisibility(View.GONE);
        remisionesLayout.setVisibility(View.GONE);

        DatosGlobales objGlobal = (DatosGlobales) this.getApplicationContext();
        for (PermisosDoc permisoDoc: objGlobal.getPermisosUsr().getListapermisosDocs()) {
            if (permisoDoc.getNombreDoc().equals("Cotización")){
                cotizacionesLayout.setVisibility(View.VISIBLE);
            } else if (permisoDoc.getNombreDoc().equals("Pedido")) {
                pedidosLayout.setVisibility(View.VISIBLE);
            } else if (permisoDoc.getNombreDoc().equals("Remisión Contado")) {
                remisionesLayout.setVisibility(View.VISIBLE);
            }
        }

        cotizacionesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                loadFragment(new DocumentosFragment(1));

            }
        });

        pedidosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                loadFragment(new DocumentosFragment(2));

            }
        });

        remisionesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    String[] permissions = new String[1];
                    permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
                    requestPermissions(permissions, CODE_GPS);
                } else {
                    // Check if location is enabled
                    if (!isLocationEnabled()) {
                        showLocationSettingsDialog();
                    } else {
                        dialog.dismiss();
                        loadFragment(new DocumentosFragment(3));
                        //lanzarFirma();
                    }
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    private void showTopDialog(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.topsettings);

        ArrayList<Productos> historial = retrieveProductListHistory();
        DatosGlobales objGlobal = (DatosGlobales) this.getApplicationContext();
        moneda = dialog.findViewById(R.id.moneda);
        conExistencia = dialog.findViewById(R.id.conExistencia);
        LinearLayout quitarCliente = dialog.findViewById(R.id.layout4);
        LinearLayout inciarLayout = dialog.findViewById(R.id.layoutIniciarSesion);
        LinearLayout cerrarLayout = dialog.findViewById(R.id.layoutCerrarSesion);
        LinearLayout layoutHistorial = dialog.findViewById(R.id.layoutHistorial);
        LinearLayout layoutExistencia = dialog.findViewById(R.id.layout2);
        LinearLayout entregaPedido = dialog.findViewById(R.id.layoutEntrega);
        LinearLayout layoutVolverVistaSupervisor = dialog.findViewById(R.id.layoutVolverVistaSupervisor);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        if (objGlobal.getMoneda() != null) {
            moneda.setChecked(objGlobal.getMoneda().equals("MXN"));
        }
        if (objGlobal.getConExistencia() != null) {
            conExistencia.setChecked(objGlobal.getConExistencia());
        }

        if (objGlobal.getCliente() != null) {
            quitarCliente.setVisibility(View.VISIBLE);
        } else {
            quitarCliente.setVisibility(View.GONE);
        }

        if (objGlobal.getPermisosUsr() == null) {
            inciarLayout.setVisibility(View.VISIBLE);
            cerrarLayout.setVisibility(View.GONE);
            entregaPedido.setVisibility(View.GONE);
            layoutVolverVistaSupervisor.setVisibility(View.GONE);
        } else {
            if (objGlobal.getPermisosUsr().getUsuario().equals("ADMON")){
                layoutVolverVistaSupervisor.setVisibility(View.VISIBLE);
            }
            entregaPedido.setVisibility(View.VISIBLE);
            cerrarLayout.setVisibility(View.VISIBLE);
            inciarLayout.setVisibility(View.GONE);
        }
        if (binding.bottomNavigationView.getSelectedItemId() == R.id.menuSearch) {
            if (!historial.isEmpty()) {
                layoutHistorial.setVisibility(View.VISIBLE);
            }
        }
        if (binding.bottomNavigationView.getSelectedItemId() == R.id.menuHome ){ //|| binding.bottomNavigationView.getSelectedItemId() == R.id.menuSearch
            layoutExistencia.setVisibility(View.VISIBLE);
        }
        layoutHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmación de borrado");
                builder.setMessage("¿Quieres borrar todo tu historial de busquedas?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        historial.removeAll(historial);
                        saveProductList(historial);
                        loadFragment(new SearchFragment());
                        dialog.dismiss();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "No se elimino nada", Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
        layoutVolverVistaSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SupervisorActivity.class);
                startActivity(i);
                finish();
                dialog.dismiss();
            }
        });
        moneda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moneda.isChecked()) {
                    objGlobal.setMoneda("MXN");
                } else {
                    objGlobal.setMoneda("USD");
                }
                List<Fragment> fragments = getSupportFragmentManager().getFragments();

                for (Fragment fragment : fragments) {
                    if (fragment instanceof OnCurrencyChangeListener) {
                        // Llama al método onCurrencyChanged de fragmentos que implementan la interfaz.
                        ((OnCurrencyChangeListener) fragment).onCurrencyChanged("cambio");
                    }
                }
                dialog.dismiss();
            }
        });
        quitarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carritoProductos != null ){
                    for (Productos produtc: carritoProductos) {
                        precioProductos(0, produtc, new PrecioProductosCallback() {
                            @Override
                            public void onPrecioProductosResponse(double precio) {
                                produtc.setPrecioSelect(precio);
                                saveProductListCar(carritoProductos);
                            }

                            @Override
                            public void onPrecioProductosError(String errorMessage) {
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                quitarCliente.setVisibility(View.GONE);
                Toast.makeText(context, "Volviendo a publico en general.", Toast.LENGTH_SHORT).show();
                objGlobal.setCliente(null);
                objGlobal.setMoneda("MXN");
                List<Fragment> fragments = getSupportFragmentManager().getFragments();

                for (Fragment fragment : fragments) {
                    if (fragment instanceof OnCurrencyChangeListener) {
                        // Llama al método onCurrencyChanged de fragmentos que implementan la interfaz.
                        ((OnCurrencyChangeListener) fragment).onCurrencyChanged("cambio");
                    }
                }
                dialog.dismiss();
            }
        });
        conExistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conExistencia.isChecked()) {
                    objGlobal.setConExistencia(true);
                } else {
                    objGlobal.setConExistencia(false);
                }
                if (binding.bottomNavigationView.getSelectedItemId() == R.id.menuHome){
                    InicioFragment fragment = (InicioFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);

                        // Llama al método onCurrencyChanged del fragmento para que actualice el ListView.
                        fragment.onCurrencyChanged("conExistencia");
                }else if (binding.bottomNavigationView.getSelectedItemId() == R.id.menuSearch){
                    SearchFragment fragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);

                    // Llama al método onCurrencyChanged del fragmento para que actualice el ListView.
                    fragment.onCurrencyChanged("conExistencia");
                }
                dialog.dismiss();
            }
        });
        inciarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cerrarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Hasta pronto: " + objGlobal.getPermisosUsr().getUsuario(), Toast.LENGTH_SHORT).show();
                objGlobal.setPermisosUsr(null);
                objGlobal.setCliente(null);
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                dialog.dismiss();
            }
        });

        entregaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarFirma();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.TOP);

    }
    public interface OnCurrencyChangeListener {
        void onCurrencyChanged(String newCurrency);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_GPS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lanzarFirma();
            } else {
                Toast.makeText(MainActivity.this, "Permiso ubicación denegado", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void lanzarFirma() {
        Intent i = new Intent(this, FirmaView.class);
        startActivity(i);
    }
    private boolean isLocationEnabled() {
        // Check if location is enabled on the device
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void showLocationSettingsDialog() {
        // Show a dialog to prompt the user to enable location
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Enable Location")
                .setMessage("Location services are disabled. Please enable them to use this app.")
                .setPositiveButton("Settings", (dialog, which) -> {
                    // Open the location settings screen
                    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(settingsIntent);
                })
                .setNegativeButton("Cancelelar", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private ArrayList<Productos> retrieveProductList() {
        SharedPreferences preferences = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(PRODUCT_LIST_KEY, null);

        Type type = new TypeToken<ArrayList<Productos>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
    private ArrayList<Productos> retrieveProductListHistory() {
        SharedPreferences preferences = this.getSharedPreferences(PREFS_NAME1, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(PRODUCT_LIST_KEY1, null);

        Type type = new TypeToken<ArrayList<Productos>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
    private void saveProductList(ArrayList<Productos> productList) {
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences(PREFS_NAME1, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(productList);

        editor.putString(PRODUCT_LIST_KEY1, json);
        editor.apply();
    }
    private void saveProductListCar(ArrayList<Productos> productList) {
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(productList);

        editor.putString(PRODUCT_LIST_KEY, json);
        editor.apply();
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        setMyImageButtonVisibility(View.VISIBLE);
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        inactivityHandler.removeCallbacksAndMessages(null);
    }
    public interface PrecioProductosCallback {
        void onPrecioProductosResponse(double precio);
        void onPrecioProductosError(String errorMessage);
    }
    private void precioProductos(int clienteID, Productos productos, PrecioProductosCallback callback) {
        String url = BuildConfig.urlWS + "/ObtenerProductos";
        StringRequest obtenerProductos = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    // Convertir el XML a objetos Producto utilizando XmlPullParser
                    try {
                        // Crear un InputStream a partir del String XML
                        InputStream inputStream = new ByteArrayInputStream(response.getBytes());

                        // Inicializar el XmlPullParser
                        XmlPullParser xmlPullParser = Xml.newPullParser();
                        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        xmlPullParser.setInput(inputStream, null);

                        ArrayList<Double> precios = new ArrayList<>();

                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("decimal".equals(tagName)) {
                                    precios.add(Double.parseDouble(xmlPullParser.nextText()));
                                }
                            }
                            eventType = xmlPullParser.next();
                        }
                        callback.onPrecioProductosResponse(precios.get(0));
                    } catch (XmlPullParserException | IOException e) {
                        Log.e("XmlParsingError", "Error al analizar el XML", e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error","Error al conectar al ws "+ error);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipoPrecio", "precioXcliente");
                params.put("nomProducto", productos.getName());
                params.put("tipo", "");
                params.put("marca", "");
                params.put("idCliente", String.valueOf(clienteID));
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(obtenerProductos);
    }
    public class checkServerStatus extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // Configuración del tiempo de espera para establecer la conexión (en milisegundos)
                connection.setConnectTimeout(5000); // 5 segundos

                // Configuración del tiempo de espera para leer datos (en milisegundos)
                connection.setReadTimeout(10000); // 10 segundos
                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    return "true";
                } else {
                    return "false";
                }
            } catch (IOException e) {
                Log.e("Error c","error e");
                return "false";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("false")){
                binding.settings.setEnabled(true);
                binding.bottomNavigationView.getMenu().getItem(0).setEnabled(false);
                binding.bottomNavigationView.getMenu().getItem(1).setEnabled(false);
                binding.bottomNavigationView.getMenu().getItem(3).setEnabled(false);
                binding.bottomNavigationView.getMenu().getItem(4).setEnabled(false);
                binding.history.setEnabled(false);
            }   else {
                loadFragment(inicioFragment);
            }
        }
    }
}