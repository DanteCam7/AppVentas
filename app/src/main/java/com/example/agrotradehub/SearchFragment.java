package com.example.agrotradehub;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.Productos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements MainActivity.OnCurrencyChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PRODUCT_LIST_KEY = "productList";
    ListView recyclerView;
    TextView sinHistorial;
    EditText txtBuscar;
    ProgressBar carga;
    ArrayList<Productos> productos;
    ArrayList<Productos> historial;
    ListViewAdapter adapter;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.rvLista);
        txtBuscar = view.findViewById(R.id.txtSearch);
        carga = view.findViewById(R.id.productosBusca);
        sinHistorial = view.findViewById(R.id.sinHistorial);
        historial = retrieveProductList();
        if (historial != null && !historial.isEmpty()) {
            // Hacer algo con la lista recuperada
            adapter = new ListViewAdapter(getContext(), historial);
            recyclerView.setAdapter(adapter);
            sinHistorial.setVisibility(View.GONE);
        }else {
            historial = new ArrayList<Productos>();
            sinHistorial.setVisibility(View.VISIBLE);
            buscarProductos("");
        }
        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 2){
                    buscarProductos(charSequence.toString());
                } else {
                    if (historial != null && !historial.isEmpty()){
                        adapter = new ListViewAdapter(getContext(), historial);
                        recyclerView.setAdapter(adapter);
                    }else {
                        recyclerView.setAdapter(null);
                        sinHistorial.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
                Productos producto;
                if (productos == null && txtBuscar.getText().toString().isEmpty()){
                    producto = historial.get(i);
                }else {
                    producto = productos.get(i);
                }
                producto.setInHistory(true);
                if (!historial.contains(producto)){
                    Log.i("agregado", "¡Algo salió mal!");
                    historial.add(producto);
                }
                saveProductList(historial);
                objGlobal.setIdproducto(producto.getId());
                objGlobal.setNomProducto(producto.getName());
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DetallesProducto detallesProducto = new DetallesProducto();
                manager.beginTransaction()
                        .replace(R.id.frame_container, detallesProducto)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        return view;
    }

    private void buscarProductos(String busqueda) {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String url = datosGlobales.getEntorno() +"/ObtenerProductos";
        productos = new ArrayList<Productos>();
        if (!busqueda.isEmpty()) {
            sinHistorial.setVisibility(View.GONE);
            carga.setVisibility(View.VISIBLE);
            StringRequest buscarProdcutos = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                            Productos producto = null;

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
                                                producto.getPrecios().add(Double.parseDouble(xmlPullParser.nextText()) / datosGlobales.getPrecioDolar());
                                            }
                                        }
                                    }
                                } else if (eventType == XmlPullParser.END_TAG) {
                                    String tagName = xmlPullParser.getName();
                                    if ("Producto".equals(tagName) && producto != null) {
                                        productos.add(producto);
                                        producto = null;
                                    }
                                }
                                eventType = xmlPullParser.next();
                            }
                            adapter = new ListViewAdapter(getContext(), productos);
                            carga.setVisibility(View.GONE);
                            recyclerView.setAdapter(adapter);
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
                    params.put("nomProducto", busqueda);
                    params.put("tipo", "");
                    params.put("marca", "");
                    params.put("idCliente", "0");
                    return params;
                }
            };
            Volley.newRequestQueue(getContext()).add(buscarProdcutos);
        }
    }
    private void saveProductList(ArrayList<Productos> productList) {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(productList);

        editor.putString(PRODUCT_LIST_KEY, json);
        editor.apply();
    }

    private ArrayList<Productos> retrieveProductList() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(PRODUCT_LIST_KEY, null);

        Type type = new TypeToken<ArrayList<Productos>>(){}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onCurrencyChanged(String newCurrency) {
        adapter.notifyDataSetChanged();
    }
}