package com.example.agrotradehub;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agrotradehub.adapters.ListViewAdapterDocuments;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.Clientes;
import com.example.agrotradehub.models.Documentos;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatePickerDialog datePickerDialog, datePickerDialogInicio;
    ListView listadoDocumentos;
    Spinner clienteDoc;
    TextView notDocs, fechaFinal, fechaInicio;
    ProgressBar cargaDocs;
    ArrayList<Documentos> documentos;
    int tipoDoc;
    Date fechaSeleccionada, fechaSeleccionadaInicio;
    String nombreCliente = "";
    public DocumentosFragment() {
        // Required empty public constructor
    }
    public DocumentosFragment(int tipoDoc) {
        // Required empty public constructor
        this.tipoDoc = tipoDoc;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentosFragment newInstance(String param1, String param2) {
        DocumentosFragment fragment = new DocumentosFragment();
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
        View view = inflater.inflate(R.layout.fragment_documentos, container, false);
        DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
        listadoDocumentos = view.findViewById(R.id.listadoDocumentos);
        notDocs = view.findViewById(R.id.noDocs);
        cargaDocs = view.findViewById(R.id.docsCarga);
        cargaDocs.setVisibility(View.VISIBLE);
        fechaFinal = view.findViewById(R.id.fechaTermino);
        fechaInicio = view.findViewById(R.id.fechaInicio);
        clienteDoc = view.findViewById(R.id.clienteDoc);
        fechaInicio.setText(getTodaysDate());
        fechaFinal.setText(getOneMonthLater());
        initDatePicker();
        initDatePickerInicio();
        obtenerClientes();
        fechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogInicio.show();
            }
        });
        clienteDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Clientes clientes = (Clientes) clienteDoc.getSelectedItem();
                nombreCliente = clientes.getRazonsocial();
                obtenerDocumentos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listadoDocumentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                objGlobal.setDocumentoid(String.valueOf(documentos.get(i).getCIDDOCUMENTO()));
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DetalleDocumento detalle = new DetalleDocumento();
                manager.beginTransaction()
                        .replace(R.id.frame_container, detalle)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        return view;
    }
    private String getOneMonthLater() {
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();

        // Resta un mes
        calendar.setTime(fechaActual);
        calendar.add(Calendar.MONTH, -1);

        // Obtiene la nueva fecha
        Date fechaNueva = calendar.getTime();
        fechaSeleccionada = fechaNueva;

        String date = makeDateString(calendar.get(Calendar.DAY_OF_MONTH), (calendar.get(Calendar.MONTH)) + 1, calendar.get(Calendar.YEAR));
        return date;
    }
    private String  getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        fechaSeleccionadaInicio = cal.getTime();
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                Date compareDate = calendar.getTime();
                if (compareDate.compareTo(fechaSeleccionadaInicio) <= 0) {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    fechaFinal.setText(date);
                    fechaSeleccionada = calendar.getTime();
                    obtenerDocumentos();
                }else {
                    Toast.makeText(getContext(),"Selecciona una fecha anterior: "+fechaSeleccionadaInicio, Toast.LENGTH_LONG).show();
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month -1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.YEAR, 2010);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        long fechaMin = cal.getTimeInMillis();
        long fechaMax = System.currentTimeMillis();

        datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(fechaMax);
        datePickerDialog.getDatePicker().setMinDate(fechaMin);
    }
    private void initDatePickerInicio() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY,23);
                calendar.set(Calendar.MINUTE, 59);
                Date compareDate = calendar.getTime();
                if (compareDate.after(fechaSeleccionada)){
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    fechaInicio.setText(date);
                    fechaSeleccionadaInicio = calendar.getTime();
                    obtenerDocumentos();
                }else {
                    Toast.makeText(getContext(),"No puedes seleccionar esta fecha", Toast.LENGTH_LONG).show();
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.YEAR, 2010);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        long fechaMin = cal.getTimeInMillis();
        long fechaMax = System.currentTimeMillis();

        datePickerDialogInicio = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
        datePickerDialogInicio.getDatePicker().setMaxDate(fechaMax);
        datePickerDialogInicio.getDatePicker().setMinDate(fechaMin);
    }
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }
    private String getMonthFormat(int month) {
        if (month == 1 )
            return "ENE";
        if (month == 2 )
            return "FEB";
        if (month == 3 )
            return "MAR";
        if (month == 4 )
            return "ABR";
        if (month == 5 )
            return "MAY";
        if (month == 6 )
            return "JUN";
        if (month == 7 )
            return "JUL";
        if (month == 8 )
            return "AGO";
        if (month == 9 )
            return "SEP";
        if (month == 10 )
            return "OCT";
        if (month == 11 )
            return "NOV";
        if (month == 12 )
            return "DIC";

        return "JAN";
    }
    private void obtenerDocumentos() {
        DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
        String url = objGlobal.getEntorno() + "/ReadOrders";
        documentos  = new ArrayList<Documentos>();
        if (nombreCliente == null || nombreCliente.equals("Seleccionar Cliente")) nombreCliente = "";
        SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");

        // Convierte la fecha actual al formato deseado
        String fechaFormateada = formato.format(fechaSeleccionadaInicio);
        String fechaFormateada2 = formato.format(fechaSeleccionada);
        StringRequest obtenerDocs = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

                        Documentos documento = null;
                        String fecha = "";

                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("lstOrders".equals(tagName)) {
                                    documento = new Documentos();
                                } else if ("dcFolio".equals(tagName) && documento != null) {
                                    documento.setCFOLIO(Integer.parseInt(xmlPullParser.nextText()));
                                } else if ("clCode".equals(tagName) && documento != null) {
                                    documento.setCCODIGOCLIENTE(xmlPullParser.nextText());
                                } else if ("agCode".equals(tagName) && documento != null) {
                                    documento.setCCODIGOAGENTE(xmlPullParser.nextText());
                                } else if ("dcId".equals(tagName) && documento != null) {
                                    documento.setCIDDOCUMENTO(Integer.parseInt(xmlPullParser.nextText()));
                                } else if ("dcFecha".equals(tagName) && documento != null) {
                                    fecha = xmlPullParser.nextText();
                                    SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
                                    documento.setCFECHA(formatoEntrada.parse(fecha));
                                } /*else if ("dcSurtido".equals(tagName) && documento != null) {
                                    int surtido = Integer.parseInt(xmlPullParser.nextText());
                                    if (surtido == 1){
                                        documento.setSurtido(true);
                                    }else {
                                        documento.setSurtido(false);
                                    }
                                */ else if ("dcTotal".equals(tagName) && documento != null) {
                                    documento.setCTOTAL(Double.parseDouble(xmlPullParser.nextText()));
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("lstOrders".equals(tagName) && documento != null) {
                                    documentos.add(documento);
                                    documento = null;
                                }
                            }
                            eventType = xmlPullParser.next();
                        }
                        if (documentos.isEmpty()) {
                            notDocs.setText("No hay coincidencias");
                            notDocs.setVisibility(View.VISIBLE);
                        } else {
                            notDocs.setVisibility(View.GONE);
                        }
                        if (getContext() != null) {
                            ListViewAdapterDocuments adapter = new ListViewAdapterDocuments(getContext(), documentos);
                            cargaDocs.setVisibility(View.GONE);
                            listadoDocumentos.setAdapter(adapter);
                        }
                    } catch (XmlPullParserException | IOException e) {
                        Log.e("XmlParsingError", "Error al analizar el XML", e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al conectar con el WS " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sdcIdDe", String.valueOf(tipoDoc));
                params.put("sFecIni", fechaFormateada2);
                params.put("sFecFin", fechaFormateada);
                params.put("sAgen", objGlobal.getPermisosUsr().getAgCode());
                params.put("sClie", nombreCliente);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(obtenerDocs);
    }
    private void obtenerClientes() {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String url = datosGlobales.getEntorno() + "/ObtenerClientes";
        final ArrayList<Clientes> clientes = new ArrayList<>();
        clientes.add(new Clientes("Seleccionar Cliente"));
        StringRequest obtenerClientes = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

                        Clientes cliente = null;

                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Cliente".equals(tagName)) {
                                    cliente = new Clientes();
                                } else if ("razonsocial".equals(tagName) && cliente != null) {
                                    cliente.setRazonsocial(xmlPullParser.nextText());
                                } else if ("id".equals(tagName) && cliente != null) {
                                    cliente.setId(Integer.parseInt(xmlPullParser.nextText()));
                                } else if ("codigo".equals(tagName) && cliente != null) {
                                    cliente.setCodigo(xmlPullParser.nextText());
                                } else if ("rfc".equals(tagName) && cliente != null) {
                                    cliente.setRFC(xmlPullParser.nextText());
                                } else if ("moneda".equals(tagName) && cliente != null) {
                                    cliente.setMoneda(Integer.parseInt(xmlPullParser.nextText()));
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Cliente".equals(tagName) && cliente != null) {
                                    clientes.add(cliente);
                                    cliente = null;
                                }
                            }
                            eventType = xmlPullParser.next();
                        }
                        if (getContext() != null) {
                            ArrayAdapter<Clientes> f = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, clientes);
                            clienteDoc.setAdapter(f);
                        }
                    } catch (XmlPullParserException | IOException e) {
                        Log.e("XmlParsingError", "Error al analizar el XML", e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" +
                        "Error al conectar al WS " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", "");
                params.put("idAgente", String.valueOf(datosGlobales.getPermisosUsr().getAgID()));
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(obtenerClientes);
    }
}