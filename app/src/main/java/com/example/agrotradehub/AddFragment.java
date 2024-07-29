package com.example.agrotradehub;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agrotradehub.adapters.ListViewClientAdapter;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.Clientes;
import com.example.agrotradehub.models.Productos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PREFS_NAME = "MyCarProducts";
    private static final String PRODUCT_LIST_KEY = "carProductList";
    private boolean usuarioHaInteractuado = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int dato = 0;
    ListView listadoClientes;
    TextView notClient;
    EditText searchClient;
    Button addClient;
    ProgressBar carga;
    ArrayList<Clientes> clientes;
    ArrayList<Productos> carritoProductos;
    RequestQueue requestQueue;
    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
        searchClient = view.findViewById(R.id.txtSearchClient);
        listadoClientes = view.findViewById(R.id.listadoClientes);
        notClient = view.findViewById(R.id.noCliente);
        addClient = view.findViewById(R.id.addClient);
        carga = view.findViewById(R.id.clientesCarga);
        requestQueue = Volley.newRequestQueue(getContext());
        carga.setVisibility(View.VISIBLE);
        searchClient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (usuarioHaInteractuado) {
                    ObtenerClientes();
                }
                usuarioHaInteractuado = true;
            }
        });

        listadoClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Clientes cliente = clientes.get(i);
                ConfirmarCliente(cliente);
            }
        });

        addClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (objGlobal.getPermisosUsr() == null){
                    Toast.makeText(getContext(), "Inicia sesión primero", Toast.LENGTH_LONG).show();
                }else {
                    addOneClient addOneClient = new addOneClient();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, addOneClient);
                    transaction.commit();
                }

            }
        });
        ObtenerClientes();
        return view;
    }

    private void ObtenerClientes() {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String url = datosGlobales.getEntorno() + "/ReadCostumers";
        if (datosGlobales.getPermisosUsr() != null) {
            dato = datosGlobales.getPermisosUsr().getAgID();
            clientes = new ArrayList<Clientes>();
            StringRequest obtenerClientes = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response != null) {
                        // Convertir el XML a objetos Producto utilizando XmlPullParser
                        try {
                            clientes.clear();
                            // Crear un InputStream a partir del String XML
                            InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                            // Inicializar el XmlPullParser
                            XmlPullParser xmlPullParser = Xml.newPullParser();
                            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                            xmlPullParser.setInput(inputStream, null);

                            Clientes cliente = null;

                            int eventType = xmlPullParser.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_TAG) {
                                    String tagName = xmlPullParser.getName();
                                    if ("lstClientes".equals(tagName)) {
                                        cliente = new Clientes();
                                    } else if ("clName".equals(tagName) && cliente != null) {
                                        cliente.setRazonsocial(xmlPullParser.nextText());
                                    } else if ("clId".equals(tagName) && cliente != null) {
                                        cliente.setId(Integer.parseInt(xmlPullParser.nextText()));
                                    } else if ("clCode".equals(tagName) && cliente != null) {
                                        cliente.setCodigo(xmlPullParser.nextText());
                                    } else if ("clRFC".equals(tagName) && cliente != null) {
                                        cliente.setRFC(xmlPullParser.nextText());
                                    } else if ("clMoneda".equals(tagName) && cliente != null) {
                                        cliente.setMoneda(Integer.parseInt(xmlPullParser.nextText()));
                                    }
                                } else if (eventType == XmlPullParser.END_TAG) {
                                    String tagName = xmlPullParser.getName();
                                    if ("lstClientes".equals(tagName) && cliente != null) {
                                        clientes.add(cliente);
                                        cliente = null;
                                    }
                                }
                                eventType = xmlPullParser.next();
                            }
                            if (clientes.isEmpty()) {
                                notClient.setText("No hay coincidencias");
                                notClient.setVisibility(View.VISIBLE);
                            } else {
                                notClient.setVisibility(View.GONE);
                            }
                            if (getContext() != null) {
                                ListViewClientAdapter adapter = new ListViewClientAdapter(getContext(), clientes);
                                carga.setVisibility(View.GONE);
                                listadoClientes.setAdapter(adapter);
                            }
                        } catch (XmlPullParserException | IOException e) {
                            Log.e("XmlParsingError", "Error al analizar el XML", e);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error clientes " + error, Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("sCliente", searchClient.getText().toString());
                    params.put("iagId", String.valueOf(dato));
                    return params;
                }
            };
            Object requestTag = "inigioTAG";
            obtenerClientes.setTag(requestTag);
            requestQueue.add(obtenerClientes);
        }else {
            carga.setVisibility(View.GONE);
            notClient.setText("Necesitas iniciar sesión para ver tus clientes");
            notClient.setVisibility(View.VISIBLE);
        }
    }
    private void ConfirmarCliente(Clientes cliente){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmar cliente");
        builder.setMessage("Establecer como cliente a: "+cliente.getRazonsocial()).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
                if (cliente.getMoneda() == 1) {
                    objGlobal.setMoneda("MXN");
                } else {
                    objGlobal.setMoneda("USA");
                }
                carritoProductos = retrieveProductList();
                if (carritoProductos != null ){
                    for (Productos produtc: carritoProductos) {
                        precioProductos(cliente.getId(), produtc, new PrecioProductosCallback() {
                            @Override
                            public void onPrecioProductosResponse(double precio) {
                                produtc.setPrecioSelect(precio);
                                saveProductList(carritoProductos);
                            }

                            @Override
                            public void onPrecioProductosError(String errorMessage) {
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                objGlobal.setCliente(cliente);
                Toast.makeText(getContext(), "Cliente :" + cliente.getRazonsocial() + " aceptado", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(),"No se selecciono ningun cliente",Toast.LENGTH_LONG).show();
                dialogInterface.dismiss();
            }
        }).show();
    }
    public interface PrecioProductosCallback {
        void onPrecioProductosResponse(double precio);
        void onPrecioProductosError(String errorMessage);
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

    private void precioProductos(int clienteID, Productos productos, PrecioProductosCallback callback) {
        DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
        String url = objGlobal.getEntorno() + "/ObtenerProductos";
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
                Toast.makeText(getContext(), "asbgf " + error, Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(getContext()).add(obtenerProductos);
    }

    @Override
    public void onDestroy() {
        if (requestQueue != null) {
            Object requestTag = "inigioTAG";
            requestQueue.cancelAll(requestTag);
        }
        super.onDestroy();
    }
}