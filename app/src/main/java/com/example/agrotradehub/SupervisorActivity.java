package com.example.agrotradehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.Switch;
import android.widget.Toast;

import com.example.agrotradehub.adminFragments.agentesFragment;
import com.example.agrotradehub.databinding.ActivityLoginBinding;
import com.example.agrotradehub.databinding.ActivitySupervisorBinding;
import com.example.agrotradehub.global.DatosGlobales;

public class SupervisorActivity extends AppCompatActivity {
    public ActivitySupervisorBinding binding;
    agentesFragment agentesFragment = new agentesFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupervisorBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_supervisor);
        setContentView(binding.getRoot());
        loadFragment(agentesFragment);
        binding.settingsS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTopDialog(view.getContext());
            }
        });

        binding.productosAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SupervisorActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void setMyImageButtonVisibility(int visibility) {
        binding.settingsS.setVisibility(visibility);
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
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_containerS, fragment);
        transaction.commit();
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

            cerrarLayout.setVisibility(View.VISIBLE);
            inciarLayout.setVisibility(View.GONE);
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
                Toast.makeText(view.getContext(),"Hasta pronto: supervisor",Toast.LENGTH_SHORT).show();
                //Toast.makeText(view.getContext(),"Hasta pronto: "+objGlobal.getPermisosUsr().getUsuario(),Toast.LENGTH_SHORT).show();
                objGlobal.setPermisosUsr(null);
                dialog.dismiss();
                Intent i = new Intent(view.getContext(), LoginActivity.class);
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