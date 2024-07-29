package com.example.agrotradehub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agrotradehub.databinding.ActivityLoginBinding;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.Permisos;
import com.example.agrotradehub.models.PermisosDoc;
import com.example.agrotradehub.models.PermisosUsr;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    String usuario, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_login);
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false); // Para evitar que el usuario lo cierre
        binding.btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (validar()) {
                        if (binding.edtUser.getText().toString().equals("almacen")) {
                            hideKeyboard();
                            toastCorrecto("Bienvenido: almacen");
                            Intent i = new Intent(getApplicationContext(), AlmacenActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            loginWS();
                            progressDialog.show();
                        }
                    } else {
                        toastIncorrecto("Por favor, complete todos los campos.");
                    }
                } catch (Exception e) {
                    toastIncorrecto("Se ha producido un error al intentar loguearte : " + e.getMessage());
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.edtPassword.getWindowToken(), 0);
    }

    public void toastCorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    private boolean validar() {
        boolean retorno = true;
        usuario = binding.edtUser.getText().toString();
        password = binding.edtPassword.getText().toString();
        if (usuario.isEmpty()) {
            binding.txtInputUser.setError("Ingrese su usuario y/o correo electrónico");
            retorno = false;
        } else {
            binding.txtInputUser.setErrorEnabled(false);
        }
        if (password.isEmpty()) {
            binding.txtInputPassword.setError("Ingrese su contraseña");
            retorno = false;
        } else {
            binding.txtInputPassword.setErrorEnabled(false);
        }
        return retorno;
    }

    private void loginWS() {
        DatosGlobales datosGlobales = (DatosGlobales) getApplication().getApplicationContext();
        String ws = datosGlobales.getEntorno() + "/Login";
        StringRequest loginRqs = new StringRequest(Request.Method.POST, ws, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = factory.newPullParser();

                        // Configurar el parser con la cadena XML
                        parser.setInput(new StringReader(response));
                        PermisosUsr permisosUsr = new PermisosUsr();
                        ArrayList<Permisos> permisos = new ArrayList<>();
                        ArrayList<PermisosDoc> permisosDoc = new ArrayList<>();
                        PermisosDoc permisoDoc = null;
                        String tagName = null;
                        // Procesar el XML
                        int eventType = parser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlPullParser.START_TAG:
                                    tagName = parser.getName();
                                    if ("IDUSUARIO".equals(tagName)) {
                                        permisosUsr.setIdUsuario(Integer.parseInt(parser.nextText()));
                                        // Haz algo con idUsuario
                                    } else if ("agId".equals(tagName)) {
                                        permisosUsr.setAgID(Integer.parseInt(parser.nextText()));
                                        // Haz algo con idUsuario
                                    } else if ("agName".equals(tagName)) {
                                        permisosUsr.setNombre(parser.nextText());
                                        // Haz algo con nombre
                                    } else if ("agCode".equals(tagName)) {
                                        permisosUsr.setAgCode(parser.nextText());
                                        // Haz algo con nombre
                                    } else if ("Usuario".equals(tagName)) {
                                        permisosUsr.setUsuario(parser.nextText());
                                        // Haz algo con nombre
                                    } else if ("fnCode".equals(tagName)) {
                                        permisosUsr.setFnCode(parser.nextText());
                                        // Haz algo con nombre
                                    }else if ("showCost".equals(tagName)) {
                                        Permisos permiso = new Permisos();
                                        int activo = Integer.parseInt(parser.nextText());
                                        if (activo == 1){
                                            permiso.setActivo(true);
                                        }else {
                                            permiso.setActivo(false);
                                        }
                                        permiso.setName("showCost");
                                        permisos.add(permiso);
                                        // Haz algo con nivel
                                    } else if ("proteCost".equals(tagName)) {
                                        permisosUsr.setProteCost(Double.parseDouble(parser.nextText()));
                                        // Haz algo con nivel
                                    } else if ("p".equals(tagName)) {
                                        permisoDoc = new PermisosDoc();
                                    } else if ("CIDCONCEPTODOCUMENTO".equals(tagName) && permisoDoc != null) {
                                        permisoDoc.setIdDoc(Integer.parseInt(parser.nextText()));
                                    } else if ("CNOMBRECONCEPTO".equals(tagName) && permisoDoc != null) {
                                        permisoDoc.setNombreDoc(parser.nextText());
                                    } else if ("Impres".equals(tagName) && permisoDoc != null) {
                                        permisoDoc.setImpresion(Integer.parseInt(parser.nextText()));
                                    } else if ("Creaci".equals(tagName) && permisoDoc != null) {
                                        permisoDoc.setCreacion(Integer.parseInt(parser.nextText()));
                                    } else if ("Cancel".equals(tagName) && permisoDoc != null) {
                                        permisoDoc.setCancelacion(Integer.parseInt(parser.nextText()));
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    String taga = parser.getName();
                                    if ("p".equals(taga) && permisoDoc != null) {
                                        permisosDoc.add(permisoDoc);
                                        permisoDoc = null;
                                    }
                                    break;
                            }
                            eventType = parser.next();
                        }
                        permisosUsr.setListapermisos(permisos);
                        permisosUsr.setListapermisosDocs(permisosDoc);
                        datosGlobales.setPermisosUsr(permisosUsr);
                        if (datosGlobales.getPermisosUsr().getUsuario().equals("error")){
                            toastIncorrecto("Revisa tu usuario ó contraseña");
                            datosGlobales.setPermisosUsr(null);
                            progressDialog.dismiss();
                        }else{
                            if (datosGlobales.getPermisosUsr().getFnCode() != null && datosGlobales.getPermisosUsr().getFnCode().equals("VEN")) {
                                toastCorrecto("Bienvenido: " + datosGlobales.getPermisosUsr().getNombre().toString());
                                hideKeyboard();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            } else if (datosGlobales.getPermisosUsr().getUsuario().equals("ADMON")) {
                                hideKeyboard();
                                toastCorrecto("Bienvenido: " + datosGlobales.getPermisosUsr().getUsuario().toString());
                                Intent i = new Intent(getApplicationContext(), SupervisorActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                toastIncorrecto("Revisa tu usuario ó contraseña");
                            }
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("VolleyError", "Error al conectar al servidor", e);
                        Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("VolleyError", "Error al conectar al Web Service", error);
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sUsr", usuario);
                params.put("sPwd", password);
                return params;
            }
        };
        int timeoutMillis = 10000; //10 segundos
        loginRqs.setRetryPolicy(new DefaultRetryPolicy(
                timeoutMillis,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Agregar la solicitud a la cola
        Volley.newRequestQueue(getApplicationContext()).add(loginRqs);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aun no has iniciado sesión");
        builder.setIcon(R.drawable.candado);
        builder.setMessage("Estas seguro de salir").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}