package com.example.agrotradehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.agrotradehub.databinding.ActivityAlmacenBinding;
import com.example.agrotradehub.databinding.ActivitySupervisorBinding;
import com.example.agrotradehub.global.DatosGlobales;

public class AlmacenActivity extends AppCompatActivity {
    ActivityAlmacenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlmacenBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_almacen);
        setContentView(binding.getRoot());

        binding.settingsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTopDialog(view.getContext());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showTopDialog(Context context) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.topsettings);

        DatosGlobales objGlobal = (DatosGlobales) getApplication().getApplicationContext();
        LinearLayout quitarCliente = dialog.findViewById(R.id.layout4);
        LinearLayout inciarLayout = dialog.findViewById(R.id.layoutIniciarSesion);
        LinearLayout cerrarLayout = dialog.findViewById(R.id.layoutCerrarSesion);
        LinearLayout layout2 = dialog.findViewById(R.id.layout2);
        LinearLayout layout1 = dialog.findViewById(R.id.layout1);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        quitarCliente.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout1.setVisibility(View.GONE);

        if (objGlobal.getPermisosUsr() == null){
            inciarLayout.setVisibility(View.VISIBLE);
            cerrarLayout.setVisibility(View.GONE);
        } else {
            cerrarLayout.setVisibility(View.VISIBLE);
            inciarLayout.setVisibility(View.GONE);
        }
        inciarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), LoginActivity.class);
                startActivity(i);
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cerrarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Hasta pronto: "+objGlobal.getPermisosUsr().getUsuario(),Toast.LENGTH_SHORT).show();
                objGlobal.setPermisosUsr(null);
                dialog.dismiss();
                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.TOP);

    }
}