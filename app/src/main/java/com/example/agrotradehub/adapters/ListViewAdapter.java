package com.example.agrotradehub.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agrotradehub.R;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.Productos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Productos> {

    public ListViewAdapter(@NonNull Context context, List<Productos> productos) {
        super(context, R.layout.list_item_productos,productos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Productos producto = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_productos,parent,false);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        TextView txtTitle = convertView.findViewById(R.id.nombreCarrito);
        ImageView enHistorial = convertView.findViewById(R.id.enHistorial);
        ImageView imgProduct = convertView.findViewById(R.id.imagenProducto);
        TextView txtPrice = convertView.findViewById(R.id.cantidadCarrito);

        DatosGlobales datosGlobales = (DatosGlobales) getContext().getApplicationContext();
        String formateado = "";
        boolean encontrado = false;
        if (datosGlobales.getCliente() != null){
            if (datosGlobales.getMoneda() == null) {
                datosGlobales.setMoneda("MXN");
                for (double doub: producto.getPrecios()) {
                    if (doub != 0){
                        formateado = decimalFormat.format(doub);
                        encontrado = true;
                        break;
                    }
                }
            } else {
                if (datosGlobales.getMoneda().equals("MXN")) {
                    for (double doub: producto.getPrecios()) {
                        if (doub != 0){
                            formateado = decimalFormat.format(doub);
                            encontrado = true;
                            break;
                        }
                    }
                } else {
                    for (double doub: producto.getPrecios()) {
                        if (doub != 0){
                            formateado = decimalFormat.format(doub/ datosGlobales.getPrecioDolar());
                            encontrado = true;
                            break;
                        }
                    }
                }
            }
            if (!encontrado){
                formateado = decimalFormat.format(0.00);
            }
        }else {
            if (datosGlobales.getMoneda() == null) {
                datosGlobales.setMoneda("MXN");
                formateado = decimalFormat.format(producto.getPrecios().get(3));
            } else {
                if (datosGlobales.getMoneda().equals("MXN")) {
                    formateado = decimalFormat.format(producto.getPrecios().get(3));
                } else {
                    formateado = decimalFormat.format(producto.getPrecios().get(3)/ datosGlobales.getPrecioDolar());
                }
            }
        }
        if (!producto.getFotoProducto().isEmpty()) {
            byte[] imageData = hexStringToByteArray(producto.getFotoProducto());

            // Decodifica el arreglo de bytes en un Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imgProduct.setImageBitmap(bitmap);
        }
        txtTitle.setText(producto.getName());
        txtPrice.setText("$"+formateado);
        if (producto.isInHistory()){
            enHistorial.setVisibility(View.VISIBLE);
        }
        return convertView;
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
}
