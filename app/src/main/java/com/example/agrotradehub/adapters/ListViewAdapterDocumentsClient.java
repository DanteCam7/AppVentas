package com.example.agrotradehub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agrotradehub.R;
import com.example.agrotradehub.models.Documentos;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListViewAdapterDocumentsClient extends ArrayAdapter<Documentos> {

    public ListViewAdapterDocumentsClient(@NonNull Context context, ArrayList<Documentos> documentos) {
        super(context, R.layout.list_item_productos_docs_client,documentos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Documentos documento = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_productos_docs_client,parent,false);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = documento.getCFECHA();

        TextView docID = convertView.findViewById(R.id.iddocumento);
        ImageView pendiente = convertView.findViewById(R.id.iconoApoyo);
        TextView totalDoc = convertView.findViewById(R.id.totalDocumento);
        TextView fechaDoc = convertView.findViewById(R.id.fechaDocumento);
        TextView nombreAgente = convertView.findViewById(R.id.nombreAgente);
        String formateado = "";
        formateado = decimalFormat.format(documento.getCTOTAL());
        docID.setText(String.valueOf(documento.getCFOLIO()));
        totalDoc.setText("$"+formateado);
        fechaDoc.setText(formatoEntrada.format(fecha).toString());
        nombreAgente.setText("Agente: "+documento.getCCODIGOAGENTE());
        if (documento.isCCANCELADO()){
            pendiente.setImageResource(R.drawable.cancel);
        }else if (documento.isSurtido()){
            pendiente.setImageResource(R.drawable.surtido);
        } else {
            pendiente.setImageResource(R.drawable.pendiente);
        }
        return convertView;
    }
}
