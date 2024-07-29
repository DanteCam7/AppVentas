package com.example.agrotradehub;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.example.agrotradehub.models.DetalleDoc;
import com.example.agrotradehub.models.Documentos;
import com.example.agrotradehub.models.Permisos;
import com.example.agrotradehub.models.PermisosDoc;
import com.example.agrotradehub.models.Productos;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalleDocumento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleDocumento extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MainActivity mainActivity;
    private SupervisorActivity supervisorActivity;
    DetalleDoc detalleDoc = new DetalleDoc();
    TextView tipoDoc, docRazonsocial, docTotal, serieDoc, fechaDoc, docRFC, docSubtotal, docIEPS, docIVA;
    TableLayout tableLayout;
    Button shareDocument;
    ProgressBar detallesDocCarga;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isAceptado -> {
                if (isAceptado) Toast.makeText(getContext(), "permisos concedidos", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getContext(), "permisos denegados", Toast.LENGTH_SHORT).show();
            }
    );
    public DetalleDocumento() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleDocumento.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleDocumento newInstance(String param1, String param2) {
        DetalleDocumento fragment = new DetalleDocumento();
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
        }else if (context instanceof SupervisorActivity){
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
        View view = inflater.inflate(R.layout.fragment_detalle_documento, container, false);
        tipoDoc = view.findViewById(R.id.tipoDoc);
        docRazonsocial = view.findViewById(R.id.docRazonsocial);
        docTotal = view.findViewById(R.id.docTotal);
        detallesDocCarga = view.findViewById(R.id.detallesDocCarga);
        serieDoc = view.findViewById(R.id.serieDoc);
        fechaDoc = view.findViewById(R.id.fechaDoc);
        docRFC = view.findViewById(R.id.docRFC);
        docSubtotal = view.findViewById(R.id.docSubtotal);
        docIEPS = view.findViewById(R.id.docIEPS);
        docIVA = view.findViewById(R.id.docIVA);
        tableLayout = view.findViewById(R.id.tableLayout);
        shareDocument = view.findViewById(R.id.shareDocument);
        detallesDocCarga.setVisibility(View.VISIBLE);
        Activity activity = getActivity();
        if (activity != null && activity.getClass().equals(MainActivity.class)){
            mainActivity.setMyImageButtonVisibility(View.GONE);
        }else {
            supervisorActivity.setMyImageButtonVisibility(View.GONE);
            supervisorActivity.binding.productosAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            });
        }
        detailDocument();
        shareDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarPermisos(view);
            }
        });
        return view;
    }
    private void verificarPermisos(View view){
        OutputStream fos;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                ContentResolver resolver = getActivity().getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,detalleDoc.getCFOLIO()+".pdf");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"application/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS+File.separator+"/AgrotradeHub/PDF");
                Uri pdfUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(Objects.requireNonNull(pdfUri));
                Document documento = new Document();
                PdfWriter.getInstance(documento, fos);
                documento.open();

                Paragraph titulo = new Paragraph(detalleDoc.getCNOMBRECONCEPTO()+"\n",
                        FontFactory.getFont("arial", 22, Font.BOLD));
                documento.add(titulo);
                Paragraph tipo = new Paragraph(detalleDoc.getCSERIEDOCUMENTO()+"\n",
                        FontFactory.getFont("arial", 16));
                documento.add(tipo);
                SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
                Paragraph fecha = new Paragraph(formatoEntrada.format(detalleDoc.getDocFecha())+"\n\n",
                        FontFactory.getFont("arial", 16));
                documento.add(fecha);
                Paragraph razonSocial = new Paragraph(detalleDoc.getCRAZONSOCIAL()+"\n",
                        FontFactory.getFont("arial", 18, Font.BOLD));
                documento.add(razonSocial);
                Paragraph rfc = new Paragraph(detalleDoc.getCRFC()+"\n\n",
                        FontFactory.getFont("arial", 16));
                documento.add(rfc);
                PdfPTable tabla = new PdfPTable(7);
                float[] columnWidths = {2f, 2f, 3f, 3f, 1.2f, 1.2f, 2f}; // Valores en puntos
                tabla.setWidths(columnWidths);
                tabla.addCell("Cantidad");
                tabla.addCell("Unidad");
                tabla.addCell("Descripción");
                tabla.addCell("Valor unit.");
                tabla.addCell("% IEPS");
                tabla.addCell("% IVA");
                tabla.addCell("Importe");
                double subtotal = 0;
                for (Productos pro: detalleDoc.getProductosDetalles()) {
                    tabla.addCell(String.valueOf(pro.getExistencia2()));
                    tabla.addCell(pro.getUnidad());
                    tabla.addCell(pro.getName());
                    tabla.addCell("$"+String.valueOf(pro.getPrecioSelect()/pro.getExistencia2()));
                    tabla.addCell(String.valueOf(pro.getMovImpp2ieps()));
                    tabla.addCell(String.valueOf(pro.getMovImpp1iva()));
                    TextView importe = new TextView(getContext());
                    subtotal = subtotal + pro.getImporte();
                    tabla.addCell("$" + String.valueOf(pro.getImporte()));
                }
                documento.add(tabla);
                Paragraph espacio = new Paragraph("\n",
                        FontFactory.getFont("arial", 18, Font.BOLD));
                documento.add(espacio);
                Paragraph subtotaltxt = new Paragraph("Subtotal: $"+subtotal+"\n",
                        FontFactory.getFont("arial", 18));
                subtotaltxt.setAlignment(Element.ALIGN_CENTER);
                documento.add(subtotaltxt);
                Paragraph ieps = new Paragraph("I.E.P.S.: $"+detalleDoc.getCIMPUESTO2IEPS()+"\n",
                        FontFactory.getFont("arial", 18));
                ieps.setAlignment(Element.ALIGN_CENTER);
                documento.add(ieps);
                Paragraph iva = new Paragraph("I.V.A.: $"+detalleDoc.getCIMPUESTO1IVA()+"\n\n",
                        FontFactory.getFont("arial", 18));
                iva.setAlignment(Element.ALIGN_CENTER);
                documento.add(iva);
                PdfPTable total = new PdfPTable(1);
                PdfPCell cell = new PdfPCell(new Phrase("Total: $"+detalleDoc.getDocTotal(), FontFactory.getFont("arial", 18, Font.BOLD)));
                cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM); // Establecer los bordes superior e inferior
                cell.setBorderWidthTop(1); // Establecer el ancho del borde superior
                cell.setBorderWidthBottom(1); // Establecer el ancho del borde inferior
                cell.setHorizontalAlignment(1);
                total.addCell(cell);
                documento.add(total);

                documento.close();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                intent.putExtra(Intent.EXTRA_TEXT,"Compartiendo un archivo PDF");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Archivo PDF compartido");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(intent, "Compartir archivo PDF"));

            } else {
                if (ContextCompat.checkSelfPermission(
                        getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "permisos concedidos", Toast.LENGTH_SHORT).show();
                    createPDF();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Snackbar.make(view, "Este permiso no ah sido concedido, y es necesario para crear el archivo", Snackbar.LENGTH_INDEFINITE).setAction("Aceptar", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    });
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        }catch (Exception e){

        }
    }

    private void createPDF() {
        try {
            String carpeta = "/AgrotradeHub/PDF";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + carpeta;

            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
                Toast.makeText(getContext(), "Carpeta creada", Toast.LENGTH_SHORT).show();
            }
            File archivo = new File(dir, "usuario"+detalleDoc.getCFOLIO()+".pdf");
            FileOutputStream fos = new FileOutputStream(archivo);
            Document documento = new Document();
            PdfWriter.getInstance(documento, fos);
            documento.open();

            Paragraph titulo = new Paragraph(detalleDoc.getCNOMBRECONCEPTO()+"\n",
                    FontFactory.getFont("arial", 22, Font.BOLD));
            documento.add(titulo);
            Paragraph tipo = new Paragraph(detalleDoc.getCSERIEDOCUMENTO()+"\n",
                    FontFactory.getFont("arial", 16));
            documento.add(tipo);
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
            Paragraph fecha = new Paragraph(formatoEntrada.format(detalleDoc.getDocFecha())+"\n\n",
                    FontFactory.getFont("arial", 16));
            documento.add(fecha);
            Paragraph razonSocial = new Paragraph(detalleDoc.getCRAZONSOCIAL()+"\n",
                    FontFactory.getFont("arial", 18, Font.BOLD));
            documento.add(razonSocial);
            Paragraph rfc = new Paragraph(detalleDoc.getCRFC()+"\n\n",
                    FontFactory.getFont("arial", 16));
            documento.add(rfc);
            PdfPTable tabla = new PdfPTable(7);
            float[] columnWidths = {2f, 2f, 3f, 3f, 1.2f, 1.2f, 2f}; // Valores en puntos
            tabla.setWidths(columnWidths);
            tabla.addCell("Cantidad");
            tabla.addCell("Unidad");
            tabla.addCell("Descripción");
            tabla.addCell("Valor unit.");
            tabla.addCell("% IEPS");
            tabla.addCell("% IVA");
            tabla.addCell("Importe");
            double subtotal = 0;
            for (Productos pro: detalleDoc.getProductosDetalles()) {
                tabla.addCell(String.valueOf(pro.getExistencia2()));
                tabla.addCell(pro.getUnidad());
                tabla.addCell(pro.getName());
                tabla.addCell(String.valueOf(pro.getPrecioSelect()/pro.getExistencia2()));
                tabla.addCell(String.valueOf(pro.getMovImpp2ieps()));
                tabla.addCell(String.valueOf(pro.getMovImpp1iva()));
                TextView importe = new TextView(getContext());
                subtotal = subtotal + pro.getImporte();
                tabla.addCell("$" + String.valueOf(pro.getImporte()));
            }
            documento.add(tabla);
            Paragraph espacio = new Paragraph("\n",
                    FontFactory.getFont("arial", 18, Font.BOLD));
            documento.add(espacio);
            Paragraph subtotaltxt = new Paragraph("Subtotal: "+subtotal+"\n",
                    FontFactory.getFont("arial", 18));
            subtotaltxt.setAlignment(Element.ALIGN_CENTER);
            documento.add(subtotaltxt);
            Paragraph ieps = new Paragraph("I.E.P.S.: "+detalleDoc.getCIMPUESTO2IEPS()+"\n",
                    FontFactory.getFont("arial", 18));
            ieps.setAlignment(Element.ALIGN_CENTER);
            documento.add(ieps);
            Paragraph iva = new Paragraph("I.V.A.: "+detalleDoc.getCIMPUESTO1IVA()+"\n\n",
                    FontFactory.getFont("arial", 18));
            iva.setAlignment(Element.ALIGN_CENTER);
            documento.add(iva);
            PdfPTable total = new PdfPTable(1);
            PdfPCell cell = new PdfPCell(new Phrase("Total: "+detalleDoc.getDocTotal(), FontFactory.getFont("arial", 18, Font.BOLD)));
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM); // Establecer los bordes superior e inferior
            cell.setBorderWidthTop(1); // Establecer el ancho del borde superior
            cell.setBorderWidthBottom(1); // Establecer el ancho del borde inferior
            cell.setHorizontalAlignment(1);
            total.addCell(cell);
            documento.add(total);

            documento.close();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            Uri pdfUri = FileProvider.getUriForFile(getContext(), "com.example.agrotradehub.fileprovider", new File(archivo.getAbsolutePath()));
            intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            intent.putExtra(Intent.EXTRA_TEXT,"Compartiendo un archivo PDF");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Archivo PDF compartido");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Compartir archivo PDF"));

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (DocumentException e){
            e.printStackTrace();
        }
    }

    private void detailDocument() {
        DatosGlobales objGlobal = (DatosGlobales) getActivity().getApplicationContext();
        String url = objGlobal.getEntorno() + "/ReadOrder";
        StringRequest obtenerDetallesDoc = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    // Convertir el XML a objetos Producto utilizando XmlPullParser
                    try {
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = factory.newPullParser();

                        // Configurar el parser con la cadena XML
                        parser.setInput(new StringReader(response));
                        ArrayList<Productos> productos = new ArrayList<>();
                        Productos producto = null;
                        String tagName = null;
                        // Procesar el XML
                        int eventType = parser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlPullParser.START_TAG:
                                    tagName = parser.getName();
                                    if ("clName".equals(tagName)) {
                                        detalleDoc.setCRAZONSOCIAL(parser.nextText());
                                        // Haz algo con idUsuario
                                    } else if ("dcObserv".equals(tagName)) {
                                        detalleDoc.setCOBSERVACIONES(parser.nextText());
                                        // Haz algo con nombre
                                    } else if ("coName".equals(tagName)) {
                                        detalleDoc.setCNOMBRECONCEPTO(parser.nextText());
                                        // Haz algo con nombre
                                    } else if ("dcSerie".equals(tagName)) {
                                        detalleDoc.setCSERIEDOCUMENTO(parser.nextText());
                                        // Haz algo con nombre
                                    } else if ("clRFC".equals(tagName)) {
                                        detalleDoc.setCRFC(parser.nextText());
                                        // Haz algo con nombre
                                    } else if ("dcFolio".equals(tagName)) {
                                        detalleDoc.setCFOLIO(Integer.parseInt(parser.nextText()));
                                    } else if ("dcTotal".equals(tagName)) {
                                        detalleDoc.setDocTotal(Double.parseDouble(parser.nextText()));
                                    } else if ("dcTax1".equals(tagName)) {
                                        detalleDoc.setCIMPUESTO1IVA(Double.parseDouble(parser.nextText()));
                                    } else if ("dcTax2".equals(tagName)) {
                                        detalleDoc.setCIMPUESTO2IEPS(Double.parseDouble(parser.nextText()));
                                    } else if ("dcFecha".equals(tagName)) {
                                        String fecha = parser.nextText();
                                        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
                                        Date fec = formatoEntrada.parse(fecha);
                                        detalleDoc.setDocFecha(fec);
                                    } else if ("lstOrderMovs".equals(tagName)) {
                                        producto = new Productos();
                                    } else if ("prName".equals(tagName) && producto != null) {
                                        producto.setName(parser.nextText());
                                    } else if ("mvImpt1".equals(tagName) && producto != null) {
                                        producto.setMovImpp1iva(Integer.parseInt(parser.nextText()));
                                    } else if ("mvImpp2".equals(tagName) && producto != null) {
                                        producto.setMovImpp2ieps(Integer.parseInt(parser.nextText()));
                                    } else if ("mvNeto".equals(tagName) && producto != null) {
                                        producto.setPrecioSelect(Double.parseDouble(parser.nextText()));
                                    } else if ("mvUnidad".equals(tagName) && producto != null) {
                                        producto.setUnidad(parser.nextText());
                                    } else if ("mvUnids".equals(tagName) && producto != null) {
                                        producto.setExistencia2(Double.parseDouble(parser.nextText()));
                                    } else if ("mvTotal".equals(tagName) && producto != null) {
                                        producto.setImporte(Double.parseDouble(parser.nextText()));
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    String taga = parser.getName();
                                    if ("lstOrderMovs".equals(taga) && producto != null) {
                                        productos.add(producto);
                                        producto = null;
                                    }
                                    break;
                            }
                            eventType = parser.next();
                        }
                        detalleDoc.setProductosDetalles(productos);
                        docRazonsocial.setText(detalleDoc.getCRAZONSOCIAL());
                        tipoDoc.setText(detalleDoc.getCNOMBRECONCEPTO());
                        serieDoc.setText(detalleDoc.getCSERIEDOCUMENTO());
                        docRFC.setText(detalleDoc.getCRFC());
                        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
                        fechaDoc.setText(formatoEntrada.format(detalleDoc.getDocFecha()));
                        double subtotal = 0;
                        for (Productos pro: detalleDoc.getProductosDetalles()) {
                            TableRow tableRow = new TableRow(getContext());
                            TextView cantidad = new TextView(getContext());
                            cantidad.setText(String.valueOf(pro.getExistencia2()));
                            cantidad.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            tableRow.addView(cantidad);
                            TextView unidad = new TextView(getContext());
                            unidad.setText(String.valueOf(pro.getUnidad()));
                            unidad.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            tableRow.addView(unidad);
                            TextView descripcion = new TextView(getContext());
                            descripcion.setText(pro.getName());
                            descripcion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            tableRow.addView(descripcion);
                            TextView valorUnit = new TextView(getContext());
                            String valorUnit1= "$"+String.valueOf(pro.getPrecioSelect()/pro.getExistencia2());
                            valorUnit.setText(valorUnit1);
                            valorUnit.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                            valorUnit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            tableRow.addView(valorUnit);
                            TextView ieps = new TextView(getContext());
                            ieps.setText(String.valueOf(pro.getMovImpp2ieps()));
                            ieps.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                            ieps.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            tableRow.addView(ieps);
                            TextView iva = new TextView(getContext());
                            iva.setText(String.valueOf(pro.getMovImpp1iva()));
                            iva.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                            iva.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            tableRow.addView(iva);
                            TextView importe = new TextView(getContext());
                            subtotal = subtotal + pro.getImporte();
                            importe.setText("$" + String.valueOf(pro.getImporte()));
                            importe.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                            importe.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            tableRow.addView(importe);
                            tableLayout.addView(tableRow);
                        }
                        docSubtotal.setText("Subtotal       $"+subtotal);
                        docIEPS.setText("I.E.P.S        $"+detalleDoc.getCIMPUESTO2IEPS());
                        docIVA.setText("I.V.A.      "+detalleDoc.getCIMPUESTO1IVA());
                        docTotal.setText("Total     $"+detalleDoc.getDocTotal());
                        tipoDoc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        detallesDocCarga.setVisibility(View.GONE);
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
                Log.e("nose", "error: "+error);
                Toast.makeText(getContext(), "Error al conectar con el WS " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sdcId", objGlobal.getDocumentoid());
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(obtenerDetallesDoc);
    }
}