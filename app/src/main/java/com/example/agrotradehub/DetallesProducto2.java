package com.example.agrotradehub;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetallesProducto2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallesProducto2 extends Fragment {

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
    int idCliente = 0;
    TextView nombre, link, codigo, precio, costo, cantidadtxt, cuantos, ingrediente, unidad, ivatxt, iepstxt, establecerPeecio, sinHistorialActivo;
    EditText ivaedit, iepsedit, cantidad, detalle;
    ImageButton addButton, añadir, remove;
    ImageView imgProducto;
    ArrayList<Productos> carProductos;
    ProgressBar cargaDetalles;
    Productos producto = new Productos();
    int disponibles;
    int contador = 0;
    Double iva;
    Double ieps;
    Double precioSelect = 0.0, cantidad2 = 0.0;
    Double cantidad1 = 0.0;
    boolean conprecio = false;

    public DetallesProducto2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetallesProducto2.
     */
    // TODO: Rename and change types and number of parameters
    public static DetallesProducto2 newInstance(String param1, String param2) {
        DetallesProducto2 fragment = new DetallesProducto2();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_detalles_producto2, container, false);
        imgProducto = view.findViewById(R.id.image_slider);
        addButton = view.findViewById(R.id.agregar);
        nombre = view.findViewById(R.id.nombre);
        precio = view.findViewById(R.id.precio);
        ivatxt = view.findViewById(R.id.ivaTxt);
        ivaedit = view.findViewById(R.id.iva2);
        iepstxt = view.findViewById(R.id.iepsTxt);
        iepsedit = view.findViewById(R.id.ieps);
        cantidadtxt = view.findViewById(R.id.cantidadTxt);
        cantidad = view.findViewById(R.id.cantidad);
        detalle = view.findViewById(R.id.detalle);


        establecerPeecio = view.findViewById(R.id.establecerPeecio);
        /*costo = view.findViewById(R.id.costo);
        sinHistorialActivo = view.findViewById(R.id.sinHistorialActivo);
        cargaDetalles = view.findViewById(R.id.cargaDetalles);
        cargaDetalles.setVisibility(View.VISIBLE);
        cuantos.setText(String.valueOf(contador));
        carProductos = retrieveProductList();*/

        mainActivity.setMyImageButtonVisibility(View.GONE);
        if (carProductos == null) {
            // Hacer algo con la lista recuperada
            carProductos = new ArrayList<Productos>();
        }
        if (datosGlobales.getPermisosUsr() != null) {
            establecerPeecio.setVisibility(View.VISIBLE);
        }
        obtenerProducto();
        precio.setBackgroundColor(0xFF4A235A);
        establecerPeecio.setBackground(null);
        precio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                precio.setBackgroundColor(0xFF4A235A);
                establecerPeecio.setBackground(null);
            }
        });

        establecerPeecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                establecerPeecio.setBackgroundColor(0xFF4A235A);
                precio.setBackground(null);
            }
        });
        /*añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contador < disponibles) {
                    contador++;
                    cuantos.setText(String.valueOf(contador));
                } else {
                    Toast.makeText(getContext(), "No hay productos disponibles.", Toast.LENGTH_LONG).show();
                }
            }
        });*/
        /*link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri _link = Uri.parse(link.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, _link);
                startActivity(intent);
            }
        });*/
        /*remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contador > 0) {
                    contador--;
                    cuantos.setText(String.valueOf(contador));
                }
            }
        });*/

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean encontrado = false;
                int totalPosible = 0;
                String cantidad2 = cantidad.getText().toString();
                cantidad1 = Double.parseDouble(cantidad2) ;
                if (cantidad1 == 0.0) {
                    Toast.makeText(getContext(), "Debes agregar por lo menos 1 producto.", Toast.LENGTH_LONG).show();
                } else {
                    /*for (Productos produc : carProductos) {
                        totalPosible = produc.getExistencia() - produc.getTotalCarrito();
                        if (produc.getName().equals(producto.getName()) && totalPosible <= 0 ) {
                            encontrado = true;
                            break;
                        }
                    }*/
                    if (!encontrado){
                        if ((establecerPeecio.getBackground() != null && precio.getBackground() == null) || (establecerPeecio.getBackground() != null && precio.getVisibility() == View.GONE)) {
                            establecerNuevoPrecioxProducto();
                        } else if (establecerPeecio.getBackground() == null && precio.getBackground() != null) {
                            precioSelect = producto.getPrecios().get(3);
                            if (precioSelect != 0) {
                                ConfirmarPrecio();
                            } else {
                                Toast.makeText(getContext(), "No puedes agregar este producto.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (precio.getBackground() != null ) {
                                precioSelect = producto.getPrecios().get(0);
                            }
                            if (precioSelect != 0 ) {
                                ConfirmarPrecio();
                            } else {
                                Toast.makeText(getContext(), "Elige un precio mayor a $0.00", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else {
                        if (totalPosible > 0){
                            Toast.makeText(getContext(), "Solo puedes agregar: "+totalPosible, Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getContext(), "Ya no puedes agregar mas productos a tu carrito", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        if (datosGlobales.getPermisosUsr() != null) {
            for (int i = 0; i < datosGlobales.getPermisosUsr().getListapermisos().size(); i++) {
                if (datosGlobales.getPermisosUsr().getListapermisos().get(i).getName() == "showCost" && datosGlobales.getPermisosUsr().getListapermisos().get(i).isActivo()) {
                    costo.setVisibility(View.VISIBLE);
                }
            }
        }
        return view;
    }

    private void obtenerProducto() {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        Toast.makeText(getContext(),datosGlobales.getNomProducto() , Toast.LENGTH_SHORT).show();
        String url = datosGlobales.getEntorno() + "/ObtenerProductos";
        if (datosGlobales.getCliente() != null) {
            idCliente = datosGlobales.getCliente().getId();
        } else {
            idCliente = 0;
        }
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
                        String hexImagen = "";
                        ArrayList<Double> precios = new ArrayList<>();
                        boolean produ = false;
                        String productName = "";
                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();

                                if ("Name".equals(tagName)) {
                                    productName = xmlPullParser.nextText();
                                    if(productName.equals(datosGlobales.getNomProducto())){
                                        producto.setName(productName);
                                    }
                                } else if ("id".equals(tagName) & productName.equals(datosGlobales.getNomProducto())) {
                                    producto.setId(xmlPullParser.nextText());
                                } /*else if ("fichaTecnica".equals(tagName) & productName.equals(datosGlobales.getNomProducto())) {
                                    link.setText(xmlPullParser.nextText());
                                } else if ("ingredienteActivo".equals(tagName) & productName.equals(datosGlobales.getNomProducto())) {
                                    ingrediente.setText(xmlPullParser.nextText());
                                } else if ("unidad".equals(tagName) & productName.equals(datosGlobales.getNomProducto())) {
                                    unidad.setText(xmlPullParser.nextText());
                                } */else if ("decimal".equals(tagName) & productName.equals(datosGlobales.getNomProducto())) {
                                    Log.e("precio", "Nuevo precio");
                                    precios.add(Double.parseDouble(xmlPullParser.nextText()));
                                } /*else if ("costo".equals(tagName) & productName.equals(datosGlobales.getNomProducto())) {
                                    producto.setCosto(Double.parseDouble(xmlPullParser.nextText()));
                                } */else if ("fotoProducto".equals(tagName) && producto != null & productName.equals(datosGlobales.getNomProducto())) {
                                    hexImagen = xmlPullParser.nextText();
                                } /*else if ("existencia".equals(tagName) & productName.equals(datosGlobales.getNomProducto())) {
                                    disponibles = Integer.parseInt(xmlPullParser.nextText());
                                } */else if ("iva".equals(tagName) ) {
                                    iva = Double.parseDouble(xmlPullParser.nextText());
                                } else if ("ieps".equals(tagName) ) {
                                    ieps = Double.parseDouble(xmlPullParser.nextText());
                                }

                            }
                            eventType = xmlPullParser.next();
                        }
                        nombre.setText(producto.getName());
                        //codigo.setText(producto.getId());
                        producto.setPrecios(precios);
                        //producto.setExistencia(disponibles);
                        //costo.setText("Costo: $" + producto.getCosto());
                        producto.setFotoProducto(hexImagen);
                        byte[] imageData = hexStringToByteArray(hexImagen);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        imgProducto.setImageBitmap(bitmap);
                        cantidad.setText("0" );
                        //iepstxt.setText("IEPS: " + ieps + "%");
                        //ivatxt.setText("IVA: " + iva + "%");
                        for (int i = 0; i < producto.getPrecios().size()-1; i++) {
                            if (producto.getPrecios().get(i) != 0) {
                                conprecio = true;
                                break;
                            }
                        }
                        if (conprecio) {
                            if (datosGlobales.getMoneda().equals("MXN")) {
                                precio.setText("Ultima Compra: $" + precios.get(0).toString());
                            } else {
                                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                String formateado = decimalFormat.format((precios.get(0) / datosGlobales.getPrecioDolar()));
                                String formateado2 = decimalFormat.format((precios.get(1) / datosGlobales.getPrecioDolar()));
                                String formateado3 = decimalFormat.format((precios.get(2) / datosGlobales.getPrecioDolar()));
                                precio.setText("Ultima Compra: $" + formateado);
                            }
                        } else {
                            if (producto.getPrecios().get(3) > 0){
                                precio.setText("Ultima venta: $"+producto.getPrecios().get(3));
                            }else {
                                if (datosGlobales.getPermisosUsr() != null){
                                    establecerPeecio.setBackgroundColor(0xFF4A235A);
                                    establecerPeecio.setVisibility(View.VISIBLE);
                                }else {
                                    sinHistorialActivo.setVisibility(View.VISIBLE);
                                }
                                precio.setVisibility(View.GONE);
                            }
                        }
                        //cargaDetalles.setVisibility(View.GONE);
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
                params.put("nomProducto", datosGlobales.getNomProducto());
                params.put("tipo", "");
                params.put("marca", "");
                params.put("idCliente", String.valueOf(idCliente));
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(obtenerProductos);
    }
    public byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] data = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }

        return data;
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

        Type type = new TypeToken<ArrayList<Productos>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private void ConfirmarPrecio() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formateado;
        DatosGlobales datosGlobales = (DatosGlobales) getContext().getApplicationContext();
        if (datosGlobales.getMoneda() == null) {
            datosGlobales.setMoneda("MXN");
            formateado = decimalFormat.format(precioSelect);
        } else {
            if (datosGlobales.getMoneda().equals("MXN")) {
                formateado = decimalFormat.format(precioSelect);
            } else {
                formateado = decimalFormat.format(precioSelect / datosGlobales.getPrecioDolar());
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmar precio seleccionado");
        builder.setMessage("Establecer precio: " + formateado).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                producto.setTotalCarrito(cantidad1.intValue());
                producto.setPrecioSelect(precioSelect);
                carProductos.add(producto);
                showOrHideBadge(true);
                saveProductList(carProductos);
                dialogInterface.dismiss();
                Toast.makeText(getContext(), "Producto agregado con exito", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void establecerNuevoPrecioxProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Configura el diseño del diálogo
        builder.setTitle("Nuevo precio");
        DatosGlobales datosGlobales = (DatosGlobales) getContext().getApplicationContext();

        // Obtén el valor del margen desde dimens.xml
        int marginInDp = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        double precioProducto = 0.0 ; //producto.getCosto() + (producto.getCosto() * (datosGlobales.getPermisosUsr().getProteCost()/100));


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
        textViewExplicacion.setText("Ingrese el precio para este articulo el cual debe ser superior a: " +precioProducto ); // Agrega tu explicación aquí
        layout.addView(textViewExplicacion);

        // Crear un EditText para el campo de entrada de texto
        EditText input = new EditText(this.getContext());
        // Aplicar los mismos márgenes al EditText
        input.setLayoutParams(layoutParams);
        layout.addView(input);

        builder.setView(layout);

        // Agrega botones al diálogo
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    double precioIngresado = Double.parseDouble(input.getText().toString());

                    precioSelect = precioIngresado;
                    ConfirmarPrecio();
                    dialog.dismiss();

                    /*if (precioIngresado > producto.getCosto()) {
                        // El precio cumple con la condición
                        // Puedes hacer algo con el precio ingresado, por ejemplo, mostrarlo en un TextView
                        precioSelect = precioIngresado;
                        ConfirmarPrecio();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        mostrarDialogoError("El preciio debe ser superior a " + producto.getCosto());
                    }*/
                } catch (NumberFormatException e) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "No ingresaste un numero", Toast.LENGTH_LONG).show();
                }

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

    public void mostrarDialogoError(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_error, null);
        builder.setView(dialogView);

        TextView textViewErrorMessage = dialogView.findViewById(R.id.textViewErrorMessage);
        Button buttonDismiss = dialogView.findViewById(R.id.buttonDismiss);

        textViewErrorMessage.setText(mensaje);

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Cierra el cuadro de diálogo
            }
        });
    }
}