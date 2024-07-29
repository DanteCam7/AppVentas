package com.example.agrotradehub.observers;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.agrotradehub.LoginActivity;
import com.example.agrotradehub.global.DatosGlobales;

import java.lang.ref.WeakReference;

public class AppLifecycleObserver implements LifecycleEventObserver {
    private WeakReference<Context> contextReference;

    public AppLifecycleObserver(Context context) {
        contextReference = new WeakReference<>(context);
    }
    private static final long RESTART_DELAY_MS = 10000;
    private Handler handler = new Handler();
    private Runnable restartRunnable = new Runnable() {
        @Override
        public void run() {
            Context context = contextReference.get();
            if (context != null) {
                DatosGlobales objGlobal = (DatosGlobales) context.getApplicationContext();
                // Haz lo que necesitas con objGlobal
                objGlobal.setPermisosUsr(null);
            }
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    };
    @Override
    public void onStateChanged(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_STOP){
            Context context = contextReference.get();
            if (context != null) {
                DatosGlobales objGlobal = (DatosGlobales) context.getApplicationContext();
                // Haz lo que necesitas con objGlobal
                if (objGlobal.getPermisosUsr() != null){
                    handler.postDelayed(restartRunnable, RESTART_DELAY_MS);
                }else {
                    handler.removeCallbacks(restartRunnable);
                }
            }
        } else if (event == Lifecycle.Event.ON_START) {
            handler.removeCallbacks(restartRunnable);
        }
    }
}
