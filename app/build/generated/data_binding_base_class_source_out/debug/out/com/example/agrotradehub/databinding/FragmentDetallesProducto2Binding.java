// Generated by view binder compiler. Do not edit!
package com.example.agrotradehub.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.agrotradehub.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDetallesProducto2Binding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final ImageButton agregar;

  @NonNull
  public final EditText cantidad;

  @NonNull
  public final TextView cantidadTxt;

  @NonNull
  public final ProgressBar cargaDetalles;

  @NonNull
  public final TextView costo;

  @NonNull
  public final EditText detalle;

  @NonNull
  public final TextView establecerPeecio;

  @NonNull
  public final EditText ieps;

  @NonNull
  public final TextView iepsTxt;

  @NonNull
  public final ImageView imageSlider;

  @NonNull
  public final EditText iva2;

  @NonNull
  public final TextView ivaTxt;

  @NonNull
  public final TextView nombre;

  @NonNull
  public final TextView precio;

  @NonNull
  public final TextView sinHistorialActivo;

  private FragmentDetallesProducto2Binding(@NonNull FrameLayout rootView,
      @NonNull ImageButton agregar, @NonNull EditText cantidad, @NonNull TextView cantidadTxt,
      @NonNull ProgressBar cargaDetalles, @NonNull TextView costo, @NonNull EditText detalle,
      @NonNull TextView establecerPeecio, @NonNull EditText ieps, @NonNull TextView iepsTxt,
      @NonNull ImageView imageSlider, @NonNull EditText iva2, @NonNull TextView ivaTxt,
      @NonNull TextView nombre, @NonNull TextView precio, @NonNull TextView sinHistorialActivo) {
    this.rootView = rootView;
    this.agregar = agregar;
    this.cantidad = cantidad;
    this.cantidadTxt = cantidadTxt;
    this.cargaDetalles = cargaDetalles;
    this.costo = costo;
    this.detalle = detalle;
    this.establecerPeecio = establecerPeecio;
    this.ieps = ieps;
    this.iepsTxt = iepsTxt;
    this.imageSlider = imageSlider;
    this.iva2 = iva2;
    this.ivaTxt = ivaTxt;
    this.nombre = nombre;
    this.precio = precio;
    this.sinHistorialActivo = sinHistorialActivo;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDetallesProducto2Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDetallesProducto2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_detalles_producto2, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDetallesProducto2Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.agregar;
      ImageButton agregar = ViewBindings.findChildViewById(rootView, id);
      if (agregar == null) {
        break missingId;
      }

      id = R.id.cantidad;
      EditText cantidad = ViewBindings.findChildViewById(rootView, id);
      if (cantidad == null) {
        break missingId;
      }

      id = R.id.cantidadTxt;
      TextView cantidadTxt = ViewBindings.findChildViewById(rootView, id);
      if (cantidadTxt == null) {
        break missingId;
      }

      id = R.id.cargaDetalles;
      ProgressBar cargaDetalles = ViewBindings.findChildViewById(rootView, id);
      if (cargaDetalles == null) {
        break missingId;
      }

      id = R.id.costo;
      TextView costo = ViewBindings.findChildViewById(rootView, id);
      if (costo == null) {
        break missingId;
      }

      id = R.id.detalle;
      EditText detalle = ViewBindings.findChildViewById(rootView, id);
      if (detalle == null) {
        break missingId;
      }

      id = R.id.establecerPeecio;
      TextView establecerPeecio = ViewBindings.findChildViewById(rootView, id);
      if (establecerPeecio == null) {
        break missingId;
      }

      id = R.id.ieps;
      EditText ieps = ViewBindings.findChildViewById(rootView, id);
      if (ieps == null) {
        break missingId;
      }

      id = R.id.iepsTxt;
      TextView iepsTxt = ViewBindings.findChildViewById(rootView, id);
      if (iepsTxt == null) {
        break missingId;
      }

      id = R.id.image_slider;
      ImageView imageSlider = ViewBindings.findChildViewById(rootView, id);
      if (imageSlider == null) {
        break missingId;
      }

      id = R.id.iva2;
      EditText iva2 = ViewBindings.findChildViewById(rootView, id);
      if (iva2 == null) {
        break missingId;
      }

      id = R.id.ivaTxt;
      TextView ivaTxt = ViewBindings.findChildViewById(rootView, id);
      if (ivaTxt == null) {
        break missingId;
      }

      id = R.id.nombre;
      TextView nombre = ViewBindings.findChildViewById(rootView, id);
      if (nombre == null) {
        break missingId;
      }

      id = R.id.precio;
      TextView precio = ViewBindings.findChildViewById(rootView, id);
      if (precio == null) {
        break missingId;
      }

      id = R.id.sinHistorialActivo;
      TextView sinHistorialActivo = ViewBindings.findChildViewById(rootView, id);
      if (sinHistorialActivo == null) {
        break missingId;
      }

      return new FragmentDetallesProducto2Binding((FrameLayout) rootView, agregar, cantidad,
          cantidadTxt, cargaDetalles, costo, detalle, establecerPeecio, ieps, iepsTxt, imageSlider,
          iva2, ivaTxt, nombre, precio, sinHistorialActivo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}