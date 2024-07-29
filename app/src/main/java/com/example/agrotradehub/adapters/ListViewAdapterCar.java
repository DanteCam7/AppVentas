package com.example.agrotradehub.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.agrotradehub.DetallesProducto;
import com.example.agrotradehub.MainActivity;
import com.example.agrotradehub.R;
import com.example.agrotradehub.global.DatosGlobales;
import com.example.agrotradehub.models.Productos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListViewAdapterCar extends ArrayAdapter<Productos> {
    private Context context;
    private OnItemDeleteListener deleteListener;
    private OnQuantityChangeListener quantityChangeListener;
    private static final String PREFS_NAME = "MyCarProducts";
    private static final String PRODUCT_LIST_KEY = "carProductList";
    int totalProdCont = 0, disponibles = 0;
    ArrayList<Productos> carritoProductos;
    public ListViewAdapterCar(@NonNull Context context, ArrayList<Productos> productos) {
        super(context, R.layout.list_item_productos_car,productos);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Productos producto = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_productos_car,parent,false);
        }
        carritoProductos = retrieveProductList();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DatosGlobales objGlobal = (DatosGlobales) context.getApplicationContext();
        LinearLayout layoutCarrito = convertView.findViewById(R.id.layoutCarrito);
        TextView txtTitle = convertView.findViewById(R.id.nombreProducto);
        TextView txtTotal = convertView.findViewById(R.id.totalProductosCar);
        TextView txtPriceT = convertView.findViewById(R.id.cantidadTotal);
        TextView txtPrice = convertView.findViewById(R.id.precioU);

        ImageView imagenProductoCar = convertView.findViewById(R.id.imagenProductoCar);
        ImageButton btnAdd = convertView.findViewById(R.id.addFCar);
        ImageButton btnRemove = convertView.findViewById(R.id.removeFCar);
        Button btnDelete = convertView.findViewById(R.id.deleteProducto);

        DatosGlobales datosGlobales = (DatosGlobales) getContext().getApplicationContext();
        totalProdCont = producto.getTotalCarrito();
        disponibles = producto.getExistencia();
        String formateado;
        if (datosGlobales.getMoneda() == "MXN"){
            formateado = decimalFormat.format(producto.getPrecioSelect()*producto.getTotalCarrito());
        }else {
            formateado = decimalFormat.format((producto.getPrecioSelect()*producto.getTotalCarrito())/objGlobal.getPrecioDolar());
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Producto borrado",Toast.LENGTH_SHORT).show();
                carritoProductos.remove(position);
                saveProductList(carritoProductos);
                notifyDataSetChanged();
                if (deleteListener != null) {
                    deleteListener.onItemDeleted(position);
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalProdCont = producto.getTotalCarrito() + 1;
                if (totalProdCont <= disponibles){
                    producto.setTotalCarrito(totalProdCont);
                    carritoProductos.get(position).setTotalCarrito(totalProdCont);
                    saveProductList(carritoProductos);
                    if (quantityChangeListener != null) {
                        quantityChangeListener.onQuantityChanged();
                    }
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(),"No puedes agregar mas productos",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalProdCont = producto.getTotalCarrito()-1;
                if (totalProdCont > 0){
                    producto.setTotalCarrito(totalProdCont);
                    carritoProductos.get(position).setTotalCarrito(totalProdCont);
                    saveProductList(carritoProductos);
                    if (quantityChangeListener != null) {
                        quantityChangeListener.onQuantityChanged();
                    }
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(),"Debes eliminar el producto",Toast.LENGTH_SHORT).show();
                }

            }
        });
        layoutCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objGlobal.setIdproducto(producto.getId());
                objGlobal.setNomProducto(producto.getName());
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                DetallesProducto detallesProducto = new DetallesProducto();
                manager.beginTransaction()
                        .replace(R.id.frame_container, detallesProducto)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        if (producto.getFotoProducto() != null) {
            byte[] imageData = hexStringToByteArray(producto.getFotoProducto());

            // Decodifica el arreglo de bytes en un Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imagenProductoCar.setImageBitmap(bitmap);
        }
        txtTitle.setText(producto.getName());
        txtTotal.setText(String.valueOf(producto.getTotalCarrito()));
        txtPriceT.setText("Total: $"+formateado);
        if (objGlobal.getMoneda().equals("MXN")){
            txtPrice.setText("C/U: $"+producto.getPrecioSelect());
        }else{
            Double precioxDolar = producto.getPrecioSelect()/objGlobal.getPrecioDolar();
            String formateatre = decimalFormat.format(precioxDolar);
            txtPrice.setText("C/U: $ "+formateatre);
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
    public interface OnItemDeleteListener {
        void onItemDeleted(int position);
    }
    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }
    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }
    public void setDeleteListener(OnItemDeleteListener listener) {
        this.deleteListener = listener;
    }
    private void saveProductList(ArrayList<Productos> productList) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(productList);

        editor.putString(PRODUCT_LIST_KEY, json);
        editor.apply();
    }

    private ArrayList<Productos> retrieveProductList() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(PRODUCT_LIST_KEY, null);

        Type type = new TypeToken<ArrayList<Productos>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
