package com.example.agrotradehub;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agrotradehub.adapters.ListViewAdapter;
import com.example.agrotradehub.adapters.ListViewAdapterCar;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.Clientes;
import com.example.agrotradehub.models.Filtro;
import com.example.agrotradehub.models.PermisosDoc;
import com.example.agrotradehub.models.Productos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarFragment extends Fragment implements MainActivity.OnCurrencyChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MainActivity mainActivity;
    private static final String PREFS_NAME = "MyCarProducts";
    private static final String PRODUCT_LIST_KEY = "carProductList";
    private Handler handler = new Handler();
    private Runnable runnable;
    private long intervalMillis = 5000; // Intervalo de tiempo en milisegundos ( 5 segundos)
    ListView listProductosCar;
    TextView sinProductosCar, clienteSC, totalCarrito, articulosCarrito;
    ProgressBar cargaCar;
    Button accionCarrito, eliminarCarrito;
    ArrayList<Productos> carritoProductos;
    Double total;
    ListViewAdapterCar adapter = null;
    boolean borrar;
    boolean guardado = false;
    public CarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarFragment newInstance(String param1, String param2) {
        CarFragment fragment = new CarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    private void showOrHideBadge(boolean shouldShow) {
        if (mainActivity != null) {
            mainActivity.updateBadgeVisibility(shouldShow);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Aquí puedes realizar la verificación de los datos activos
                if (datosGlobales.getCliente() == null) {
                    // Si los datos están activos, realiza alguna acción
                    // Por ejemplo, muestra un mensaje o inicia una tarea
                    clienteSC.setText("Ningun cliente seleccionado");
                    handler.removeCallbacks(runnable);
                }
                if (guardado){
                    carritoProductos.removeAll(carritoProductos);
                    showOrHideBadge(false);
                    saveProductList(carritoProductos);
                    if (carritoProductos.isEmpty()) {
                        sinProductosCar.setVisibility(View.VISIBLE);
                    }
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    total = 0.0;

                    if (datosGlobales.getCliente() != null) {
                        clienteSC.setText(datosGlobales.getCliente().getRazonsocial());
                    } else {
                        clienteSC.setText("Ningun cliente seleccionado");
                    }
                    totalCarrito.setText("$" + decimalFormat.format(total));
                    Toast.makeText(getContext(),"Tu documento ah sido guardado con exito puedes checarlo en el historial",Toast.LENGTH_LONG).show();
                    if (adapter != null){
                        adapter.notifyDataSetChanged();
                    }
                    guardado = false;
                    cargaCar.setVisibility(View.GONE);
                }
                carritoProductos = retrieveProductList();
                cargaCar.setVisibility(View.GONE);
                if (carritoProductos != null){
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    total = 0.0;
                    for (Productos producto : carritoProductos) {
                        if (datosGlobales.getCliente() != null) {
                            total = total + (producto.getTotalCarrito() * producto.getPrecioSelect());
                            clienteSC.setText(datosGlobales.getCliente().getRazonsocial());
                        } else {
                            clienteSC.setText("Ningun cliente seleccionado");
                            total = total + (producto.getTotalCarrito() * producto.getPrecioSelect());
                        }
                    }
                    articulosCarrito.setText("Productos para comprar: "+carritoProductos.size());
                    if (datosGlobales.getMoneda() == "MXN"){
                        totalCarrito.setText("$" + decimalFormat.format(total)  + " MXN");
                    }else {
                        totalCarrito.setText("$" + decimalFormat.format(total/datosGlobales.getPrecioDolar())  + "USD");
                    }
                    adapter = new ListViewAdapterCar(getContext(),carritoProductos);
                    listProductosCar.setAdapter(adapter);
                }

                // Programa la próxima ejecución del Runnable después del intervalo
                handler.postDelayed(this, intervalMillis);
            }
        };
        // Inicia la verificación periódica
        handler.postDelayed(runnable, intervalMillis);
        listProductosCar = view.findViewById(R.id.listadoCar);
        sinProductosCar = view.findViewById(R.id.noDataCar);
        cargaCar = view.findViewById(R.id.CarCarga);
        clienteSC = view.findViewById(R.id.clienteSC);
        totalCarrito = view.findViewById(R.id.totalCarrito);
        accionCarrito = view.findViewById(R.id.accionCarrito);
        eliminarCarrito = view.findViewById(R.id.eliminarCarrito);
        articulosCarrito = view.findViewById(R.id.articulosCarro);
        sinProductosCar.setVisibility(View.GONE);
        cargaCar.setVisibility(View.VISIBLE);
        carritoProductos = retrieveProductList();
        if (carritoProductos == null || carritoProductos.isEmpty()) {
            cargaCar.setVisibility(View.GONE);
            sinProductosCar.setVisibility(View.VISIBLE);
            if (datosGlobales.getCliente() != null) {
                clienteSC.setText(datosGlobales.getCliente().getRazonsocial());
            } else {
                clienteSC.setText("Ningun cliente seleccionado");
            }
            totalCarrito.setText("$" + 0.0);
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            total = 0.0;
            for (int i = 0; i < carritoProductos.size(); i++) {
                if (carritoProductos.get(i).getPrecioSelect() == 0){
                    if (carritoProductos.get(i).getPrecios().get(0) == 0){
                        establecerNuevoPrecioxProducto(carritoProductos.get(i), i);
                    }else {
                        mostrarDialogoOpciones(carritoProductos.get(i), i);
                    }
                }
            }
            for (Productos producto : carritoProductos) {
                if (datosGlobales.getCliente() != null) {
                    total = total + (producto.getTotalCarrito() * producto.getPrecioSelect());
                    clienteSC.setText(datosGlobales.getCliente().getRazonsocial());
                } else {
                    clienteSC.setText("Ningun cliente seleccionado");
                    total = total + (producto.getTotalCarrito() * producto.getPrecioSelect());
                }
            }
            articulosCarrito.setText("Productos para comprar: "+carritoProductos.size());
            if (datosGlobales.getMoneda() == "MXN"){
                totalCarrito.setText("$" + decimalFormat.format(total)  + " MXN");
            }else {
                totalCarrito.setText("$" + decimalFormat.format(total/datosGlobales.getPrecioDolar()) + " USD");
            }
            adapter = new ListViewAdapterCar(getContext(), carritoProductos);
            adapter.setDeleteListener(new ListViewAdapterCar.OnItemDeleteListener() {
                @Override
                public void onItemDeleted(int position) {
                    carritoProductos.remove(position);
                    showOrHideBadge(false);
                    if (carritoProductos.isEmpty()) {
                        sinProductosCar.setVisibility(View.VISIBLE);
                    }
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    total = 0.0;
                    for (Productos producto : carritoProductos) {
                        if (datosGlobales.getCliente() != null) {
                            total = total + (producto.getTotalCarrito() * producto.getPrecioSelect());
                            clienteSC.setText(datosGlobales.getCliente().getRazonsocial());
                        } else {
                            clienteSC.setText("Ningun cliente seleccionado");
                            total = total + (producto.getTotalCarrito() * producto.getPrecioSelect());
                        }
                    }
                    if (datosGlobales.getMoneda() == "MXN"){
                        totalCarrito.setText("$" + decimalFormat.format(total) + " MXN");
                    }else {
                        totalCarrito.setText("$" + decimalFormat.format(total/datosGlobales.getPrecioDolar()) + " USD");
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            adapter.setOnQuantityChangeListener(new ListViewAdapterCar.OnQuantityChangeListener() {
                @Override
                public void onQuantityChanged() {
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    total = 0.0;
                    for (Productos producto : carritoProductos) {
                        if (datosGlobales.getCliente() != null) {
                            total = total + (producto.getTotalCarrito() * producto.getPrecioSelect());
                            clienteSC.setText(datosGlobales.getCliente().getRazonsocial());
                        } else {
                            clienteSC.setText("Ningun cliente seleccionado");
                            total = total + (producto.getTotalCarrito() * producto.getPrecioSelect());
                        }
                    }
                    if (datosGlobales.getMoneda() == "MXN"){
                        totalCarrito.setText("$" + decimalFormat.format(total) + " MXN");
                    }else {
                        totalCarrito.setText("$" + decimalFormat.format(total/datosGlobales.getPrecioDolar()) + " USD");
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            eliminarCarrito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirmación de borrado");
                    builder.setMessage("¿Quieres borrar todo tu carrito?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            carritoProductos.removeAll(carritoProductos);
                            showOrHideBadge(false);
                            saveProductList(carritoProductos);
                            if (carritoProductos.isEmpty()) {
                                sinProductosCar.setVisibility(View.VISIBLE);
                            }
                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            total = 0.0;

                            if (datosGlobales.getCliente() != null) {
                                clienteSC.setText(datosGlobales.getCliente().getRazonsocial());
                            } else {
                                clienteSC.setText("Ningun cliente seleccionado");
                            }
                            totalCarrito.setText("$" + decimalFormat.format(total));
                            adapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(), "No se elimino nada", Toast.LENGTH_LONG).show();
                            borrar = false;
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
            });
            listProductosCar.setAdapter(adapter);
            cargaCar.setVisibility(View.GONE);
        }
        accionCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carritoProductos == null || carritoProductos.isEmpty()){
                    Toast.makeText(getContext(), "No hay productos en tu carrito", Toast.LENGTH_LONG).show();
                }else {
                    if (datosGlobales.getPermisosUsr() != null) {
                        if (datosGlobales.getCliente() != null){
                            showBottomDialog();
                        }else {
                            Toast.makeText(getContext(), "No hay ningun client seleccionado",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        abrirInicioSesion();
                    }
                }
            }
        });
        return view;
    }
    private void mostrarDialogoOpciones(Productos producto, int j) {
        CharSequence[] opcionesChar = new CharSequence[producto.getPrecios().size()];
        for (int i = 0; i < producto.getPrecios().size(); i++) {
            opcionesChar[i] = String.valueOf(producto.getPrecios().get(i));
        }
        View messageView = getLayoutInflater().inflate(R.layout.dialog_message, null);
        TextView textMessage = messageView.findViewById(R.id.textMessage);
        textMessage.setText("Selecciona una opción para el articulo: "+producto.getName()+" con: " + producto.getTotalCarrito() + " unidades.");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCustomTitle(messageView)
                .setItems(opcionesChar,
                        (dialog, which) -> mostrarDialogoConfirmacion(producto, producto.getPrecios().get(which), j));
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void mostrarDialogoConfirmacion(Productos producto, double opcionSeleccionada, int posicion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmar selección")
                .setMessage("¿Estás seguro de seleccionar la opción " + opcionSeleccionada  + " MXN?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    carritoProductos.get(posicion).setPrecioSelect(opcionSeleccionada);
                    saveProductList(carritoProductos);
                    // Aquí puedes realizar acciones adicionales según la selección
                    // La variable "opcionSeleccionada" contiene el índice de la opción elegida
                    cargaCar.setVisibility(View.VISIBLE);
                }).setNegativeButton("No", (dialog, which) -> {
                    mostrarDialogoOpciones(producto, posicion);
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void mostrarDialogoConfirmacion2(Productos producto, double opcionSeleccionada, int posicion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmar selección")
                .setMessage("¿Estás seguro de seleccionar la opción " + opcionSeleccionada  + " MXN?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    carritoProductos.get(posicion).setPrecioSelect(opcionSeleccionada);
                    saveProductList(carritoProductos);
                    // Aquí puedes realizar acciones adicionales según la selección
                    // La variable "opcionSeleccionada" contiene el índice de la opción elegida
                    cargaCar.setVisibility(View.VISIBLE);
                }).setNegativeButton("No", (dialog, which) -> {
                    establecerNuevoPrecioxProducto(producto, posicion);
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<Productos> retrieveProductList() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(PRODUCT_LIST_KEY, null);

        Type type = new TypeToken<ArrayList<Productos>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
    private void saveProductList(ArrayList<Productos> productList) {
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(productList);

        editor.putString(PRODUCT_LIST_KEY, json);
        editor.apply();
    }
    private void abrirInicioSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Inicio de sesión necesario");
        builder.setMessage("Debes iniciar sesión para continuar").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Se cancelo el inicio de sesión", Toast.LENGTH_LONG).show();
                dialogInterface.dismiss();
            }
        }).show();

    }
    private void showBottomDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayoutcar);

        LinearLayout cotizacionesLayout = dialog.findViewById(R.id.layoutCotizacionesCar);
        LinearLayout pedidosLayout = dialog.findViewById(R.id.layoutPedidosCar);
        LinearLayout remisionesLayout = dialog.findViewById(R.id.latyutRemisionesCar);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButtonCar);

        cotizacionesLayout.setVisibility(View.GONE);
        pedidosLayout.setVisibility(View.GONE);
        remisionesLayout.setVisibility(View.GONE);

        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        for (PermisosDoc permisoDoc: datosGlobales.getPermisosUsr().getListapermisosDocs()) {
            if (permisoDoc.getNombreDoc().equals("Cotización") && permisoDoc.getCreacion() == 1){
                cotizacionesLayout.setVisibility(View.VISIBLE);
            } else if (permisoDoc.getNombreDoc().equals("Pedido") && permisoDoc.getCreacion() == 1) {
                pedidosLayout.setVisibility(View.VISIBLE);
            } else if (permisoDoc.getNombreDoc().equals("Remisión Contado") && permisoDoc.getCreacion() == 1) {
                remisionesLayout.setVisibility(View.VISIBLE);
            }
        }

        cotizacionesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mostrarDialogoConTextInput("Cotización");
            }
        });

        pedidosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                mostrarDialogoConTextInput("Pedido");

            }
        });

        remisionesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mostrarDialogoConTextInput("Remisión Contado");

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
    private void establecerNuevoPrecioxProducto(Productos producto, int posicion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Configura el diseño del diálogo
        builder.setTitle("Nuevo precio para: "+producto.getName());

        // Obtén el valor del margen desde dimens.xml
        int marginInDp = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        // Crear un LinearLayout vertical para la vista del diálogo
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Configurar los márgenes del LinearLayout
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMarginStart(marginInDp); // Margen izquierdo
        layoutParams.setMarginEnd(marginInDp);   // Margen derecho
        layout.setLayoutParams(layoutParams);
        // Crear un TextView para la explicación
        TextView textViewExplicacion = new TextView(getContext());
        textViewExplicacion.setLayoutParams(layoutParams);
        textViewExplicacion.setText("Ingrese el precio para este articulo el cual debe ser superior a MXN: " + producto.getCosto()); // Agrega tu explicación aquí
        layout.addView(textViewExplicacion);

        // Crear un EditText para el campo de entrada de texto
        EditText input = new EditText(this.getContext());
        // Aplicar los mismos márgenes al EditText
        input.setLayoutParams(layoutParams);
        layout.addView(input);

        builder.setView(layout);
        builder.setCancelable(false);

        // Agrega botones al diálogo
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    double precioIngresado = Double.parseDouble(input.getText().toString());

                    if (precioIngresado > producto.getCosto()) {
                        // El precio cumple con la condición
                        // Puedes hacer algo con el precio ingresado, por ejemplo, mostrarlo en un TextView
                        mostrarDialogoConfirmacion2(producto, precioIngresado, posicion);
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(),"El precio debe ser superior a " + producto.getCosto() + "MXN",Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "No ingresaste un numero", Toast.LENGTH_LONG).show();
                }

            }
        });

        // Muestra el diálogo
        builder.show();
    }
    private void mostrarDialogoConTextInput(String dConcepto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // Reemplaza "this" con el contexto adecuado

        // Configura el diseño del diálogo
        builder.setTitle("Observaciones");

        // Obtén el valor del margen desde dimens.xml
        int marginInDp = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        // Crear un LinearLayout vertical para la vista del diálogo
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Configurar los márgenes del LinearLayout
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMarginStart(marginInDp); // Margen izquierdo
        layoutParams.setMarginEnd(marginInDp);   // Margen derecho
        layout.setLayoutParams(layoutParams);
        // Crear un TextView para la explicación
        TextView textViewExplicacion = new TextView(getContext());
        textViewExplicacion.setLayoutParams(layoutParams);
        textViewExplicacion.setText("Ingrese la observación para: "+dConcepto+", si no hay ninguna precione solo aceptar."); // Agrega tu explicación aquí
        layout.addView(textViewExplicacion);

        // Crear un EditText para el campo de entrada de texto
        final EditText input = new EditText(getContext()); // Reemplaza "this" con el contexto adecuado
        // Aplicar los mismos márgenes al EditText
        input.setLayoutParams(layoutParams);
        layout.addView(input);

        builder.setView(layout);

        // Agrega botones al diálogo
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textoIngresado = input.getText().toString();
                SaveDocu(dConcepto, textoIngresado);
                cargaCar.setVisibility(View.VISIBLE);
                // Aquí puedes hacer algo con el texto ingresado, como mostrarlo en un TextView
                // o realizar alguna otra acción.
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Muestra el diálogo
        builder.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        handler.removeCallbacks(runnable);
    }
    private void SaveDocu(String dcConcepto, String observacion) {
        DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
        String url = objGlobal.getEntorno() + "/SaveDocuX";
        String xml = generateXml(dcConcepto, observacion);
        StringRequest guardarDocumento = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                        // Verifica si el contenido del elemento de texto es "OK"
                        boolean esOK = true;
                        if (esOK) guardado = esOK;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al guardar el documento: " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("__sxmlDocu", xml);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(guardarDocumento);
    }

    private String generateXml(String dcConcepto, String observacion) {
        if (dcConcepto.isEmpty()) dcConcepto="";
        if (observacion.isEmpty()) observacion="";
        try {
            DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
            StringWriter writer = new StringWriter();
            Date fechaActual = new Date();

            // Define el formato que deseas
            SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");

            // Convierte la fecha actual al formato deseado
            String fechaFormateada = formato.format(fechaActual);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("ISO-8859-1", true);

            // <CubixAdmin>
            xmlSerializer.startTag(null, "CubixAdmin");

            // <Docu>
            xmlSerializer.startTag(null, "Docu");

            // A continuación, agregamos todos los elementos dentro de <Docu>
            addXmlElement(xmlSerializer, "dcConcepto", dcConcepto);
            addXmlElement(xmlSerializer, "dcSerie", "serie");
            addXmlElement(xmlSerializer, "dcFolio", "1");
            addXmlElement(xmlSerializer, "clCode", datosGlobales.getCliente().getCodigo());
            addXmlElement(xmlSerializer, "agCode", datosGlobales.getPermisosUsr().getAgCode());
            addXmlElement(xmlSerializer, "dcVence", fechaFormateada);
            addXmlElement(xmlSerializer, "dcIdMoneda", String.valueOf(datosGlobales.getCliente().getMoneda()));
            addXmlElement(xmlSerializer, "dcTipoCambio", "1");
            addXmlElement(xmlSerializer, "dcRefer", "referencia");
            addXmlElement(xmlSerializer, "dcObser", observacion);
            addXmlElement(xmlSerializer, "dcUsuario", datosGlobales.getPermisosUsr().getUsuario());
            addXmlElement(xmlSerializer, "IdRefer", generarRandom());

            Log.d("XML_GENERADO", String.valueOf(carritoProductos.size()));
            for (int i = 0; i < carritoProductos.size(); i++) {
                xmlSerializer.startTag(null, "Mov");

                // Primer elemento <Mov>
                addXmlElement(xmlSerializer, "prCode", carritoProductos.get(i).getId());
                addXmlElement(xmlSerializer, "mvNum", String.valueOf(i+1));
                addXmlElement(xmlSerializer, "mvUnids", String.valueOf(carritoProductos.get(i).getTotalCarrito()));
                addXmlElement(xmlSerializer, "mvPrecio", String.valueOf(carritoProductos.get(i).getPrecioSelect()));
                addXmlElement(xmlSerializer, "mvCosto", String.valueOf(carritoProductos.get(i).getCosto()));;
                // </Mov>
                xmlSerializer.endTag(null, "Mov");
            }
            // </Docu>
            xmlSerializer.endTag(null, "Docu");

            // </CubixAdmin>
            xmlSerializer.endTag(null, "CubixAdmin");

            xmlSerializer.endDocument();
            String xmlString = writer.toString();
            Log.d("XML_GENERADO", xmlString);
            return xmlString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void addXmlElement(XmlSerializer xmlSerializer, String tagName, String text) throws Exception {
        xmlSerializer.startTag(null, tagName);
        xmlSerializer.text(text);
        xmlSerializer.endTag(null, tagName);
    }

    private String generarRandom(){
        Random random = new Random();

        // Longitud del número aleatorio
        int longitud = 10;

        // Generar un número aleatorio de longitud 10
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            int digit = random.nextInt(10); // Generar un dígito aleatorio entre 0 y 9
            randomNumber.append(digit);
        }

        // Convertir el resultado a una cadena de texto
        String randomString = randomNumber.toString();
        return randomString;
    }

    @Override
    public void onCurrencyChanged(String newCurrency) {
        adapter.notifyDataSetChanged();
    }
}