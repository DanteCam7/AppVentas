package com.example.agrotradehub.adminFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.agrotradehub.BuildConfig;
import com.example.agrotradehub.DetalleDocumento;
import com.example.agrotradehub.MainActivity;
import com.example.agrotradehub.R;
import com.example.agrotradehub.SupervisorActivity;
import com.example.agrotradehub.adapters.ListViewAdapterDocuments;
import com.example.agrotradehub.adapters.ListViewAdapterDocumentsClient;
import com.example.agrotradehub.adapters.ListViewClientAdapter;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.Agentes;
import com.example.agrotradehub.models.Clientes;
import com.example.agrotradehub.models.Documentos;
import com.example.agrotradehub.models.Filtro;

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
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link agentesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class agentesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatePickerDialog datePickerDialog, datePickerDialogInicio;
    private boolean usuarioHaInteractuado = false;
    ProgressBar cargadocsSuper;
    private SupervisorActivity supervisorActivity;
    Spinner clienteDoc, tipoDoc;
    TextView fechaInicioSup, fechaTerminoSup, notDocumentSup;
    AutoCompleteTextView agenteDoc;
    ListView listDocsSuper;
    Date fechaSeleccionada, fechaSeleccionadaInicio;
    String nombreCliente = "";
    ArrayList<Documentos> documentos;
    ArrayList<String> tipoDocumentos = new ArrayList<>();
    int tipoDocnumber = 1;
    String codigoAgente = "";

    public agentesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment agentesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static agentesFragment newInstance(String param1, String param2) {
        agentesFragment fragment = new agentesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SupervisorActivity){
            supervisorActivity = (SupervisorActivity) context;
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
        DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_agentes, container, false);
        cargadocsSuper = view.findViewById(R.id.cargadocsSuper);
        clienteDoc = view.findViewById(R.id.clientesDoc);
        listDocsSuper = view.findViewById(R.id.listDocsSuper);
        agenteDoc = view.findViewById(R.id.agentesDoc);
        fechaTerminoSup = view.findViewById(R.id.fechaTerminoSup);
        fechaInicioSup = view.findViewById(R.id.fechaInicioSup);
        notDocumentSup = view.findViewById(R.id.notDocumentSup);
        tipoDoc = view.findViewById(R.id.tipoDoc);
        fechaInicioSup.setText(getTodaysDate());
        fechaTerminoSup.setText(getOneMonthLater());
        tipoDocumentos.clear();
        tipoDocumentos.add("Cotización");
        tipoDocumentos.add("Pedido");
        tipoDocumentos.add("Remisión");
        supervisorActivity.binding.productosAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        initDatePicker();
        initDatePickerInicio();
        ObtenerAgentes();
        obtenerDocumentos();
        fechaTerminoSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        fechaInicioSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogInicio.show();
            }
        });
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, tipoDocumentos);
        tipoDoc.setAdapter(adapterTipos);
        tipoDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        agenteDoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    codigoAgente = "";
                    obtenerDocumentos();

                }
            }
        });
        agenteDoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Agentes agente = (Agentes) adapterView.getItemAtPosition(i);
                codigoAgente = agente.getCCODIGOAGENTE();
                ObtenerClientes(agente.getCIDAGENTE());
                obtenerDocumentos();
            }
        });
        tipoDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (usuarioHaInteractuado){
                    tipoDocnumber = i+1;
                    obtenerDocumentos();
                }
                usuarioHaInteractuado = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        listDocsSuper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                objGlobal.setDocumentoid(String.valueOf(documentos.get(i).getCIDDOCUMENTO()));
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                DetalleDocumento detalle = new DetalleDocumento();
                manager.beginTransaction()
                        .replace(R.id.frame_containerS, detalle)
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

    private String getTodaysDate() {
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
                    fechaTerminoSup.setText(date);
                    fechaSeleccionada = calendar.getTime();
                    obtenerDocumentos();
                } else {
                    Toast.makeText(getContext(), "Selecciona una fecha anterior: " + fechaSeleccionadaInicio, Toast.LENGTH_LONG).show();
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month - 1;
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
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date compareDate = calendar.getTime();
                if (compareDate.after(fechaSeleccionada)) {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    fechaInicioSup.setText(date);
                    fechaSeleccionadaInicio = calendar.getTime();
                    obtenerDocumentos();
                } else {
                    Toast.makeText(getContext(), "No puedes seleccionar esta fecha", Toast.LENGTH_LONG).show();
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
        if (month == 1)
            return "ENE";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "ABR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AGO";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DIC";

        return "JAN";
    }

    private void ObtenerAgentes() {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String url = datosGlobales.getEntorno() + "/ObtenerAgentes";
        final ArrayList<Agentes> agentes = new ArrayList<Agentes>();
        StringRequest obtenerAgentes = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                        agentes.clear();
                        Agentes agente = null;

                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Agente".equals(tagName)) {
                                    agente = new Agentes();
                                } else if ("CIDAGENTE".equals(tagName) && agente != null) {
                                    agente.setCIDAGENTE(Integer.parseInt(xmlPullParser.nextText()));
                                } else if ("CCODIGOAGENTE".equals(tagName) && agente != null) {
                                    agente.setCCODIGOAGENTE(xmlPullParser.nextText());
                                } else if ("CNOMBREAGENTE".equals(tagName) && agente != null) {
                                    agente.setCNOMBREAGENTE(xmlPullParser.nextText());
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Agente".equals(tagName) && agente != null) {
                                    agentes.add(agente);
                                    agente = null;
                                }
                            }
                            eventType = xmlPullParser.next();
                        }
                        if (getContext() != null) {
                            ArrayAdapter<Agentes> adapterAgentes = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, agentes);
                            agenteDoc.setAdapter(adapterAgentes);
                            agenteDoc.setThreshold(1);
                        }
                    } catch (XmlPullParserException | IOException e) {
                        Log.e("XmlParsingError", "Error al analizar el XML", e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error agentes " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombreAgente", "");
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(obtenerAgentes);
    }

    private void ObtenerClientes(int idAgente) {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String url = datosGlobales.getEntorno() + "/ObtenerClientes";
        final ArrayList<Clientes> clientes = new ArrayList<Clientes>();
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
                        clientes.clear();
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
                            ArrayAdapter<Clientes> adapterClientes = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, clientes);
                            clienteDoc.setAdapter(adapterClientes);
                            clienteDoc.setVisibility(View.VISIBLE);
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
                params.put("nombre", "");
                params.put("idAgente", String.valueOf(idAgente));
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(obtenerClientes);
    }

    private void obtenerDocumentos() {
        DatosGlobales datosGlobales = (DatosGlobales) getActivity().getApplicationContext();
        String url = datosGlobales.getEntorno() + "/ObtenerDocumentos";
        documentos = new ArrayList<Documentos>();
        if (nombreCliente == null || nombreCliente.equals("Seleccionar Cliente")) nombreCliente = "";
        // Define el formato que deseas
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
                        documentos.clear();
                        Documentos documento = null;
                        String fecha = "";

                        int eventType = xmlPullParser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Documento".equals(tagName)) {
                                    documento = new Documentos();
                                } else if ("CFOLIO".equals(tagName) && documento != null) {
                                    documento.setCFOLIO(Integer.parseInt(xmlPullParser.nextText()));
                                } else if ("CCODIGOCLIENTE".equals(tagName) && documento != null) {
                                    documento.setCCODIGOCLIENTE(xmlPullParser.nextText());
                                } else if ("CCODIGOAGENTE".equals(tagName) && documento != null) {
                                    documento.setCCODIGOAGENTE(xmlPullParser.nextText());
                                } else if ("CIDDOCUMENTO".equals(tagName) && documento != null) {
                                    documento.setCIDDOCUMENTO(Integer.parseInt(xmlPullParser.nextText()));
                                } else if ("CFECHA".equals(tagName) && documento != null) {
                                    fecha = xmlPullParser.nextText();
                                    SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
                                    Date fec = formatoEntrada.parse(fecha);
                                    documento.setCFECHA(fec);
                                } else if ("CCANCELADO".equals(tagName) && documento != null) {
                                    int cancelado = Integer.parseInt(xmlPullParser.nextText());
                                    if (cancelado == 1) {
                                        documento.setCCANCELADO(true);
                                    } else {
                                        documento.setCCANCELADO(false);
                                    }
                                } else if ("Surtido".equals(tagName) && documento != null) {
                                    int surtido = Integer.parseInt(xmlPullParser.nextText());
                                    if (surtido == 1) {
                                        documento.setSurtido(true);
                                    } else {
                                        documento.setSurtido(false);
                                    }
                                } else if ("CTOTAL".equals(tagName) && documento != null) {
                                    documento.setCTOTAL(Double.parseDouble(xmlPullParser.nextText()));
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                                String tagName = xmlPullParser.getName();
                                if ("Documento".equals(tagName) && documento != null) {
                                    documentos.add(documento);
                                    documento = null;
                                }
                            }
                            eventType = xmlPullParser.next();
                        }
                        if (documentos.isEmpty()) {
                            notDocumentSup.setText("No hay coincidencias");
                            notDocumentSup.setVisibility(View.VISIBLE);
                        } else {
                            notDocumentSup.setVisibility(View.GONE);
                        }
                        if (getContext() != null) {
                            ListViewAdapterDocumentsClient adapter = new ListViewAdapterDocumentsClient(getContext(), documentos);
                            cargadocsSuper.setVisibility(View.GONE);
                            listDocsSuper.setAdapter(adapter);
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
                params.put("accionConseguir", "header");
                params.put("tipoArchivo", String.valueOf(tipoDocnumber));
                params.put("fechaInicio", fechaFormateada2);
                params.put("fechaTermino", fechaFormateada);
                params.put("codAgente", codigoAgente);
                params.put("nomCliente", nombreCliente);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(obtenerDocs);
    }
}