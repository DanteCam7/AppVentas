package com.example.agrotradehub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agrotradehub.R;
import com.example.agrotradehub.models.Clientes;

import java.util.ArrayList;

public class ListViewClientAdapter extends ArrayAdapter<Clientes> {
    public ListViewClientAdapter(@NonNull Context context, ArrayList<Clientes> clientes) {
        super(context, R.layout.list_item_cliente, clientes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Clientes clientes = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_cliente,parent,false);
        }

        TextView txtRazonSocial = convertView.findViewById(R.id.nombreCliente);

        txtRazonSocial.setText(clientes.getRazonsocial());
        return convertView;
    }
}
