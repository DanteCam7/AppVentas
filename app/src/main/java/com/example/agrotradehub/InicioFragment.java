package com.example.agrotradehub;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agrotradehub.adapters.ListViewAdapter;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.Filtro;
import com.example.agrotradehub.models.Productos;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment extends Fragment implements MainActivity.OnCurrencyChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean usuarioHaInteractuado = false, usuarioHaInteractuado2 = false, usuarioHaInteractuado3 = false;
    private static final int ELEMENTOS_POR_CARGA = 15; // Cantidad de elementos a cargar por vez
    private int paginaActual = 0; // Página actual
    String tipo;
    String marca;
    int idCliente = 0;
    ListView listado;
    ListViewAdapter adapter;
    TextView notData;
    Spinner stipo, smarca;
    ProgressBar carga;
    List<Productos> productos,productosExistencia;
    private List<Productos> productosParaMostrar;
    RequestQueue requestQueue;
    public InicioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        listado = view.findViewById(R.id.listado);
        notData = view.findViewById(R.id.noData);
        stipo = view.findViewById(R.id.tipo);
        smarca = view.findViewById(R.id.marca);
        carga = view.findViewById(R.id.productosCarga);
        productosParaMostrar = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        productos = new ArrayList<Productos>();
        productosExistencia = new ArrayList<Productos>();

        DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
        if (objGlobal.getEntorno() != null ){
            new checkServerStatus().execute(objGlobal.getEntorno());
        }
        stipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (usuarioHaInteractuado){
                    carga.setVisibility(View.VISIBLE);
                    Filtro filtro = (Filtro) stipo.getSelectedItem();
                    tipo = filtro.getName();
                    productosParaMostrar.clear();
                    llenadoProductos();
                }
                usuarioHaInteractuado = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        smarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (usuarioHaInteractuado2){
                    carga.setVisibility(View.VISIBLE);
                    Filtro filtro = (Filtro) smarca.getSelectedItem();
                    marca = filtro.getName();
                    productosParaMostrar.clear();
                    llenadoProductos();
                }
                usuarioHaInteractuado2 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Productos producto;
                if (objGlobal.getConExistencia()){
                    producto = productosExistencia.get(i);
                }else {
                    producto = productos.get(i);
                }
                objGlobal.setIdproducto(producto.getId());
                objGlobal.setNomProducto(producto.getName());
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DetallesProducto2 detallesProducto2 = new DetallesProducto2();
                //DetallesProducto detallesProducto = new DetallesProducto();
                manager.beginTransaction()
                        .replace(R.id.frame_container, detallesProducto2)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        listado.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i + i1 >= i2) {
                    // El usuario se está desplazando hacia el final, aquí puedes cargar más datos
                    // Asegúrate de controlar la paginación y la carga de datos adicionales
                    if (usuarioHaInteractuado3){
                        cargarMasDatos();
                    }
                    usuarioHaInteractuado3 = true;
                }
            }
        });
        return view;
    }
    private void cargarMasDatos(){
        int inicio = paginaActual * ELEMENTOS_POR_CARGA;
        int fin = Math.min((paginaActual + 1) * ELEMENTOS_POR_CARGA, productos.size());

        // Verifica si se han cargado todos los datos
        if (inicio >= productos.size()) {
            return; // No hay más datos para cargar
        }
        //productosParaMostrar.clear();

        // Agrega los datos al conjunto que se mostrará
        for (int i = inicio; i < fin; i++) {
            productosParaMostrar.add(productos.get(i));
        }

        // Notifica al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();

        // Incrementa la página actual
        paginaActual++;
    }
    private void llenadoProductos() {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String ws = datosGlobales.getEntorno() + "/ObtenerProductos";
        if (tipo == null || tipo.equals("Seleccionar tipo")) tipo = "";
        if (marca == null || marca.equals("Seleccionar marca")) marca = "";
        if (datosGlobales.getCliente() != null){
            idCliente = datosGlobales.getCliente().getId();
        }else {
            idCliente = 0;
        }
        StringRequest llenadoProduct = new StringRequest(Request.Method.POST, ws, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Aquí puedes procesar la respuesta XML obtenida
                if (response != null) {
                    // Convertir el XML a objetos Producto utilizando XmlPullParser
                    try {
                        // Crear un InputStream a partir del String XML
                        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                        // Inicializar el XmlPullParser
                        XmlPullParser xmlPullParser = Xml.newPullParser();
                        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        xmlPullParser.setInput(inputStream, null);
                        Productos producto = null;
                        productos.clear();

                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Producto".equals(tagName)) {
                                    producto = new Productos();
                                } else if ("Name".equals(tagName) && producto != null) {
                                    producto.setName(xmlPullParser.nextText());
                                } else if ("id".equals(tagName) && producto != null) {
                                    producto.setId(xmlPullParser.nextText());
                                } else if ("unidad".equals(tagName) && producto != null) {
                                    producto.setUnidad(xmlPullParser.nextText());
                                } else if ("existencia".equals(tagName) && producto != null) {
                                    producto.setExistencia(Integer.parseInt(xmlPullParser.nextText()));
                                } else if ("fotoProducto".equals(tagName) && producto != null) {
                                    producto.setFotoProducto(xmlPullParser.nextText());
                                } else if ("decimal".equals(tagName) && producto != null) {
                                    if (producto.getPrecios() == null) {
                                        producto.setPrecios(new ArrayList<>());
                                    }
                                    if (datosGlobales.getMoneda() == null) {
                                        datosGlobales.setMoneda("MXN");
                                        producto.getPrecios().add(Double.parseDouble(xmlPullParser.nextText()));
                                    } else {
                                        if (datosGlobales.getMoneda().equals("MXN")) {
                                            producto.getPrecios().add(Double.parseDouble(xmlPullParser.nextText()));
                                        } else {
                                            producto.getPrecios().add(Double.parseDouble(xmlPullParser.nextText()));
                                        }
                                    }
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Producto".equals(tagName) && producto != null) {
                                    if (producto.getExistencia() > 0){
                                        productosExistencia.add(producto);
                                    }
                                    if (!producto.getUnidad().equals("ACTIVIDAD")){
                                        productos.add(producto);
                                    }
                                    producto = null;
                                }
                            }
                            eventType = xmlPullParser.next();
                        }
                        if (productos.isEmpty()) {notData.setText("No hay coincidencias"); notData.setVisibility(View.VISIBLE);}
                        else {notData.setVisibility(View.GONE);}
                        if (datosGlobales.getConExistencia() == null) {
                            datosGlobales.setConExistencia(false);
                        }
                        if (getContext() != null){
                            if (datosGlobales.getConExistencia()){
                                adapter = new ListViewAdapter(getContext(), productosExistencia);
                            } else {
                                paginaActual = 0;
                                productosParaMostrar.clear();
                                adapter = new ListViewAdapter(getContext(), productosParaMostrar);
                                cargarMasDatos();
                            }
                            listado.setAdapter(adapter);
                            carga.setVisibility(View.GONE);
                        }
                    } catch (XmlPullParserException | IOException e) {
                        Log.e("XmlParsingError", "Error al analizar el XML", e);
                    }
                } else {
                    notData.setVisibility(View.VISIBLE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error al conectar al Web Service", error);
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipoPrecio", "precioXcliente");
                params.put("nomProducto", "");
                params.put("tipo", tipo);
                params.put("marca", marca);
                params.put("idCliente", String.valueOf(idCliente));
                return params;
            }
        };
        int timeoutMillis = 10000;
        llenadoProduct.setRetryPolicy(new DefaultRetryPolicy(
                timeoutMillis,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Agregar la solicitud a la cola
        Object requestTag = "inigioTAG";
        llenadoProduct.setTag(requestTag);
        requestQueue.add(llenadoProduct);
    }
    private void precioDolar() {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String ws = datosGlobales.getEntorno() + "/PrecioDolar";

        StringRequest preDolar = new StringRequest(Request.Method.POST, ws, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Aquí puedes procesar la respuesta XML obtenida
                if (response != null) {
                    // Convertir el XML a objetos Producto utilizando XmlPullParser
                    try {
                        // Crear un InputStream a partir del String XML
                        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                        // Inicializar el XmlPullParser
                        XmlPullParser xmlPullParser = Xml.newPullParser();
                        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        xmlPullParser.setInput(inputStream, null);

                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("decimal".equals(tagName)) {
                                    datosGlobales.setPrecioDolar(Double.parseDouble(xmlPullParser.nextText()));
                                }
                            }
                            eventType = xmlPullParser.next();
                        }

                    } catch (XmlPullParserException | IOException e) {
                        Log.e("XmlParsingError", "Error al analizar el XML", e);
                    }
                } else {
                    notData.setVisibility(View.VISIBLE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error al conectar al Web Service", error);
                        Toast.makeText(getContext(), "" +
                                "Error al conectar dolar al WS " + error, Toast.LENGTH_LONG).show();
                    }
                });
        // Agregar la solicitud a la cola
        Object requestTag = "inigioTAG";
        preDolar.setTag(requestTag);
        requestQueue.add(preDolar);
    }
    private void obtenerTipo() {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String url = datosGlobales.getEntorno() + "/ObtenerFiltros";
        final ArrayList<Filtro> tipos = new ArrayList<>();
        tipos.add(new Filtro("Seleccionar tipo"));
        StringRequest obtenerTipos = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

                        Filtro tipo = null;

                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Filtro".equals(tagName)) {
                                    tipo = new Filtro();
                                } else if ("name".equals(tagName) && tipo != null) {
                                    tipo.setName(xmlPullParser.nextText());
                                } else if ("id".equals(tagName) && tipo != null) {
                                    tipo.setId(Integer.parseInt(xmlPullParser.nextText()));
                                } else if ("clasificacion".equals(tagName) && tipo != null) {
                                    tipo.setClasificacion(Integer.parseInt(xmlPullParser.nextText()));
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Filtro".equals(tagName) && tipo != null) {
                                    if (!tipo.getName().isEmpty()){
                                        tipos.add(tipo);
                                    }
                                    tipo = null;
                                }
                            }
                            eventType = xmlPullParser.next();
                        }
                        if (getContext() != null){
                            ArrayAdapter<Filtro> f = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, tipos);
                            stipo.setAdapter(f);
                        }
                    } catch (XmlPullParserException | IOException e) {
                        Log.e("XmlParsingError", "Error al analizar el XML", e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "" +
                            "Error al conectar al tipo WS " + error, Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipoFiltro", String.valueOf(25));
                return params;
            }
        };
        Object requestTag = "inigioTAG";
        obtenerTipos.setTag(requestTag);
        requestQueue.add(obtenerTipos);
    }
    private void obtenerMarca() {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String url = datosGlobales.getEntorno() + "/ObtenerFiltros";
        final ArrayList<Filtro> marcas = new ArrayList<>();
        marcas.add(new Filtro("Seleccionar marca"));
        StringRequest obtenerTipos = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

                        Filtro marca = null;

                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Filtro".equals(tagName)) {
                                    marca = new Filtro();
                                } else if ("name".equals(tagName) && marca != null) {
                                    marca.setName(xmlPullParser.nextText());
                                } else if ("id".equals(tagName) && marca != null) {
                                    marca.setId(Integer.parseInt(xmlPullParser.nextText()));
                                } else if ("clasificacion".equals(tagName) && marca != null) {
                                    marca.setClasificacion(Integer.parseInt(xmlPullParser.nextText()));
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Filtro".equals(tagName) && marca != null) {
                                    marcas.add(marca);
                                    marca = null;
                                }
                            }
                            eventType = xmlPullParser.next();
                        }
                        if (getContext() != null){
                            ArrayAdapter<Filtro> f = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, marcas);
                            smarca.setAdapter(f);
                        }
                    } catch (XmlPullParserException | IOException e) {
                        Log.e("XmlParsingError", "Error al analizar el XML", e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error al conectar marca al WS " + error, Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipoFiltro", String.valueOf(29));
                return params;
            }
        };
        Object requestTag = "inigioTAG";
        obtenerTipos.setTag(requestTag);
        requestQueue.add(obtenerTipos);
    }

    @Override
    public void onCurrencyChanged(String newCurrency) {
        updateProductLists(newCurrency);
    }

    private void updateProductLists(String newCurrency) {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
            List<Productos> productListToShow = datosGlobales.getConExistencia() ? productosExistencia : productos;

            // Crea un nuevo adaptador con la lista de productos apropiada
            ListViewAdapter newAdapter = new ListViewAdapter(getContext(), productListToShow);

            // Asigna el nuevo adaptador al ListView
            listado.setAdapter(newAdapter);
    }

    @Override
    public void onDestroy() {
        if (requestQueue != null) {
            Object requestTag = "inigioTAG";
            requestQueue.cancelAll(requestTag);
        }
        super.onDestroy();
    }
    private class checkServerStatus extends AsyncTask<String, Void, String>
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
                Log.d("Response code","code: "+responseCode);
                if (responseCode == 200) {
                    return "true";
                } else {
                    return "false";
                }
            } catch (IOException e) {
                Log.e("checkServerStatus", "doInBackground - Error de conexión: " + e.getMessage());
                return "false";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("false")){
                Log.d("conexion","Aqui sin: "+BuildConfig.urlWS);
                notData.setText("Temporalmente sin servicio, intente mas tarde.");
                notData.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                notData.setVisibility(View.VISIBLE);
            }else {
                Log.d("conexion","Aqui con: "+BuildConfig.urlWS);
                carga.setVisibility(View.VISIBLE);
                obtenerTipo();
                obtenerMarca();
                precioDolar();
                llenadoProductos();
            }
        }
    }
}