package com.example.agrotradehub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agrotradehub.adapters.SpinnerAdapter;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.CountryInfo;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addOneClient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addOneClient extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner editTextCodigoPais, spinnercodigoPais, spinnerMoneda;
    EditText editTextNombre, editTextRFC, editTextCorreo, editTextTelefono, editTextTelefonoFijo, editTextCodPostal, editTextEstado,
            editTextMunicipio, editTextCiudad, editTextCalle, editTextNumeroExterior, editTextColonia, editTextNumeroInterior;
    TextInputLayout layoutNombre, layoutCP;
    Button botonEnviar;
    List<CountryInfo> countryInfoList;
    List<String> modenas = new ArrayList<>();
    boolean guardado = false;
    ProgressDialog progressDialog;
    public addOneClient() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addOneClient.
     */
    // TODO: Rename and change types and number of parameters
    public static addOneClient newInstance(String param1, String param2) {
        addOneClient fragment = new addOneClient();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                regresarAlFragmentoAnterior();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_one_client, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false); // Para evitar que el usuario lo cierre
        AssetManager assetManager = requireActivity().getAssets();
        editTextCodigoPais = view.findViewById(R.id.editTextCodigoPais);
        spinnercodigoPais = view.findViewById(R.id.spinnercodigoPais);
        spinnerMoneda = view.findViewById(R.id.spinnerMoneda);
        botonEnviar = view.findViewById(R.id.botonEnviar);
        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextRFC = view.findViewById(R.id.editTextRFC);
        editTextCorreo = view.findViewById(R.id.editTextCorreo);
        editTextTelefono = view.findViewById(R.id.editTextTelefono);
        editTextTelefonoFijo = view.findViewById(R.id.editTextTelefonoFijo);
        editTextCodPostal = view.findViewById(R.id.editTextCodPostal);
        editTextEstado = view.findViewById(R.id.editTextEstado);
        editTextMunicipio = view.findViewById(R.id.editTextMunicipio);
        editTextCiudad = view.findViewById(R.id.editTextCiudad);
        editTextCalle = view.findViewById(R.id.editTextCalle);
        editTextNumeroExterior = view.findViewById(R.id.editTextNumeroExterior);
        editTextColonia = view.findViewById(R.id.editTextColonia);
        editTextNumeroInterior = view.findViewById(R.id.editTextNumeroInterior);
        layoutNombre = view.findViewById(R.id.layouteditTextNombre);
        layoutCP = view.findViewById(R.id.layoutCP);
        modenas.add("Peso Mexicano");
        modenas.add("Dolar Americano");
        ArrayAdapter<String> adapS = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, modenas);
        spinnerMoneda.setAdapter(adapS);
        spinnerMoneda.setSelection(0);
        try {
            countryInfoList = new ArrayList<>();
            // Reemplaza "nombre_de_tu_archivo.json" con el nombre real de tu archivo JSON
            InputStream inputStream = assetManager.open("paises.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");

            // Ahora puedes analizar el JSON utilizando Gson o cualquier otra biblioteca de análisis JSON
            Gson gson = new Gson();

// Analizar el JSON
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

            // Itera a través de las claves (que son los códigos de país)
            for (String countryCode : jsonObject.keySet()) {
                JsonObject countryData = jsonObject.getAsJsonObject(countryCode);
                CountryInfo countryInfo = new CountryInfo();
                countryInfo.setSiglas(countryData.get("siglas").getAsString());
                countryInfo.setPhone(countryData.get("phone").getAsString());
                countryInfo.setUrlBandera("https://flagcdn.com/h20/"+countryData.get("siglas").getAsString().toLowerCase()+".png");
                countryInfoList.add(countryInfo);
                SpinnerAdapter adapter = new SpinnerAdapter(getContext(), countryInfoList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editTextCodigoPais.setAdapter(adapter);
                spinnercodigoPais.setAdapter(adapter);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (validar()) {
                        SaveClient();
                        progressDialog.show();
                    } else {
                        toastIncorrecto("Por favor, complete los campos requeridos.");
                    }
                } catch (Exception e) {
                    toastIncorrecto("Se ha producido un error al guardar el cliente : " + e.getMessage());
                }
            }
        });

        for (int i = 0; i < countryInfoList.size(); i++) {
            if (countryInfoList.get(i).getSiglas().equals("MX")) {
                // Establece la selección del Spinner en el índice "i"
                editTextCodigoPais.setSelection(i);
                spinnercodigoPais.setSelection(i);
                break; // Rompe el bucle una vez que se encuentra la coincidencia
            }
        }
        return view;
    }
    public void toastCorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) requireView().findViewById(R.id.ll_custom_toast_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) requireView().findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
    private boolean validar() {
        boolean retorno = true;
        if (editTextNombre.getText().toString().isEmpty()) {
            layoutNombre.setError("Razon social requerida");
            retorno = false;
        } else {
            layoutNombre.setErrorEnabled(false);
        }
        if (editTextCodPostal.getText().toString().isEmpty()) {
            layoutCP.setError("Codigo postal requerido");
            retorno = false;
        } else {
            layoutCP.setErrorEnabled(false);
        }
        return retorno;
    }

    private void SaveClient() {
        DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
        String url = objGlobal.getEntorno() + "/SaveClienteX";
        String xml = generateXml();
        Map<String, String> params = new HashMap<>();
        params.put("__sxmlClie", xml);
        StringRequest guardarCliente = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    // Verifica si el contenido del elemento de texto es "OK"
                    boolean esOK = true;
                    if (esOK) guardado = esOK;
                    toastCorrecto("Cliente: "+ editTextNombre.getText().toString()+"guardado con exito.");
                    regresarAlFragmentoAnterior();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al guardar el cliente: " + error, Toast.LENGTH_LONG).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        Volley.newRequestQueue(getContext()).add(guardarCliente);
    }
    private String generateXml() {
        try {
            DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
            StringWriter writer = new StringWriter();

            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("ISO-8859-1", true);

            // <CubixAdmin>
            xmlSerializer.startTag(null, "CubixAdmin");

            // <Docu>
            xmlSerializer.startTag(null, "Clie");

            // A continuación, agregamos todos los elementos dentro de <Docu>
            addXmlElement(xmlSerializer, "clId", "");
            addXmlElement(xmlSerializer, "clCode", "");
            addXmlElement(xmlSerializer, "clName", editTextNombre.getText().toString());
            addXmlElement(xmlSerializer, "clRFC", editTextRFC.getText().toString());
            if (spinnerMoneda.getSelectedItemPosition() == 0){
                addXmlElement(xmlSerializer, "clIdMoneda", "1");
            }else {
                addXmlElement(xmlSerializer, "clIdMoneda", "2");
            }
            addXmlElement(xmlSerializer, "clListaPrecio", "1");
            addXmlElement(xmlSerializer, "clRegimFiscal","");
            addXmlElement(xmlSerializer, "clUsoCfdi", "");
            addXmlElement(xmlSerializer, "clFormaPago", "");
            addXmlElement(xmlSerializer, "agId", String.valueOf(datosGlobales.getPermisosUsr().getAgID()));
            addXmlElement(xmlSerializer, "clCP", editTextCodPostal.getText().toString());
            addXmlElement(xmlSerializer, "clEstado", editTextEstado.getText().toString());
            addXmlElement(xmlSerializer, "clMunicipio", editTextMunicipio.getText().toString());
            addXmlElement(xmlSerializer, "clCiudad", editTextCiudad.getText().toString());
            addXmlElement(xmlSerializer, "clColonia", editTextColonia.getText().toString());
            addXmlElement(xmlSerializer, "clCalle", editTextCalle.getText().toString());
            addXmlElement(xmlSerializer, "clNumExt", editTextNumeroExterior.getText().toString());
            addXmlElement(xmlSerializer, "clNumInt", editTextNumeroInterior.getText().toString());
            CountryInfo contry = (CountryInfo) editTextCodigoPais.getSelectedItem();
            addXmlElement(xmlSerializer, "clCelular", contry.getPhone()+editTextTelefono.getText().toString());
            CountryInfo contryFijo = (CountryInfo) spinnercodigoPais.getSelectedItem();
            addXmlElement(xmlSerializer, "clTelefono", contryFijo.getPhone()+editTextTelefonoFijo.getText().toString());
            addXmlElement(xmlSerializer, "eMail", editTextCorreo.getText().toString());


            // </Docu>
            xmlSerializer.endTag(null, "Clie");

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
    // Dentro del Fragmento A
    public void regresarAlFragmentoAnterior() {
        // Obten el FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Verifica si hay fragmentos en la pila de retroceso
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Si hay fragmentos en la pila de retroceso, simplemente realiza una transacción de retroceso
            fragmentManager.popBackStack();
        } else {
            // Si no hay fragmentos en la pila de retroceso, puedes reemplazar el fragmento actual con el fragmento B
            AddFragment fragmentB = new AddFragment(); // Reemplaza 'FragmentB' con el nombre real de tu fragmento B
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragmentB)
                    .commit();
        }
    }
    private static void addXmlElement(XmlSerializer xmlSerializer, String tagName, String text) throws Exception {
        xmlSerializer.startTag(null, tagName);
        xmlSerializer.text(text);
        xmlSerializer.endTag(null, tagName);
    }
}