// Generated by view binder compiler. Do not edit!
package com.example.agrotradehub.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public final class FragmentCarBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final ProgressBar CarCarga;

  @NonNull
  public final Button accionCarrito;

  @NonNull
  public final TextView articulosCarro;

  @NonNull
  public final TextView clienteSC;

  @NonNull
  public final Button eliminarCarrito;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final ListView listadoCar;

  @NonNull
  public final TextView noDataCar;

  @NonNull
  public final TextView textCar;

  @NonNull
  public final TextView totalCarrito;

  private FragmentCarBinding(@NonNull FrameLayout rootView, @NonNull ProgressBar CarCarga,
      @NonNull Button accionCarrito, @NonNull TextView articulosCarro, @NonNull TextView clienteSC,
      @NonNull Button eliminarCarrito, @NonNull LinearLayout linearLayout,
      @NonNull ListView listadoCar, @NonNull TextView noDataCar, @NonNull TextView textCar,
      @NonNull TextView totalCarrito) {
    this.rootView = rootView;
    this.CarCarga = CarCarga;
    this.accionCarrito = accionCarrito;
    this.articulosCarro = articulosCarro;
    this.clienteSC = clienteSC;
    this.eliminarCarrito = eliminarCarrito;
    this.linearLayout = linearLayout;
    this.listadoCar = listadoCar;
    this.noDataCar = noDataCar;
    this.textCar = textCar;
    this.totalCarrito = totalCarrito;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentCarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentCarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_car, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentCarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.CarCarga;
      ProgressBar CarCarga = ViewBindings.findChildViewById(rootView, id);
      if (CarCarga == null) {
        break missingId;
      }

      id = R.id.accionCarrito;
      Button accionCarrito = ViewBindings.findChildViewById(rootView, id);
      if (accionCarrito == null) {
        break missingId;
      }

      id = R.id.articulosCarro;
      TextView articulosCarro = ViewBindings.findChildViewById(rootView, id);
      if (articulosCarro == null) {
        break missingId;
      }

      id = R.id.clienteSC;
      TextView clienteSC = ViewBindings.findChildViewById(rootView, id);
      if (clienteSC == null) {
        break missingId;
      }

      id = R.id.eliminarCarrito;
      Button eliminarCarrito = ViewBindings.findChildViewById(rootView, id);
      if (eliminarCarrito == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.listadoCar;
      ListView listadoCar = ViewBindings.findChildViewById(rootView, id);
      if (listadoCar == null) {
        break missingId;
      }

      id = R.id.noDataCar;
      TextView noDataCar = ViewBindings.findChildViewById(rootView, id);
      if (noDataCar == null) {
        break missingId;
      }

      id = R.id.textCar;
      TextView textCar = ViewBindings.findChildViewById(rootView, id);
      if (textCar == null) {
        break missingId;
      }

      id = R.id.totalCarrito;
      TextView totalCarrito = ViewBindings.findChildViewById(rootView, id);
      if (totalCarrito == null) {
        break missingId;
      }

      return new FragmentCarBinding((FrameLayout) rootView, CarCarga, accionCarrito, articulosCarro,
          clienteSC, eliminarCarrito, linearLayout, listadoCar, noDataCar, textCar, totalCarrito);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
