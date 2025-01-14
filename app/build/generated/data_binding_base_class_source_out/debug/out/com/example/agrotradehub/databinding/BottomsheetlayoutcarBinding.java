// Generated by view binder compiler. Do not edit!
package com.example.agrotradehub.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.agrotradehub.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class BottomsheetlayoutcarBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView cancelButtonCar;

  @NonNull
  public final TextView createText;

  @NonNull
  public final LinearLayout latyutRemisionesCar;

  @NonNull
  public final LinearLayout layoutCotizacionesCar;

  @NonNull
  public final LinearLayout layoutPedidosCar;

  private BottomsheetlayoutcarBinding(@NonNull LinearLayout rootView,
      @NonNull ImageView cancelButtonCar, @NonNull TextView createText,
      @NonNull LinearLayout latyutRemisionesCar, @NonNull LinearLayout layoutCotizacionesCar,
      @NonNull LinearLayout layoutPedidosCar) {
    this.rootView = rootView;
    this.cancelButtonCar = cancelButtonCar;
    this.createText = createText;
    this.latyutRemisionesCar = latyutRemisionesCar;
    this.layoutCotizacionesCar = layoutCotizacionesCar;
    this.layoutPedidosCar = layoutPedidosCar;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static BottomsheetlayoutcarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BottomsheetlayoutcarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.bottomsheetlayoutcar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BottomsheetlayoutcarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cancelButtonCar;
      ImageView cancelButtonCar = ViewBindings.findChildViewById(rootView, id);
      if (cancelButtonCar == null) {
        break missingId;
      }

      id = R.id.createText;
      TextView createText = ViewBindings.findChildViewById(rootView, id);
      if (createText == null) {
        break missingId;
      }

      id = R.id.latyutRemisionesCar;
      LinearLayout latyutRemisionesCar = ViewBindings.findChildViewById(rootView, id);
      if (latyutRemisionesCar == null) {
        break missingId;
      }

      id = R.id.layoutCotizacionesCar;
      LinearLayout layoutCotizacionesCar = ViewBindings.findChildViewById(rootView, id);
      if (layoutCotizacionesCar == null) {
        break missingId;
      }

      id = R.id.layoutPedidosCar;
      LinearLayout layoutPedidosCar = ViewBindings.findChildViewById(rootView, id);
      if (layoutPedidosCar == null) {
        break missingId;
      }

      return new BottomsheetlayoutcarBinding((LinearLayout) rootView, cancelButtonCar, createText,
          latyutRemisionesCar, layoutCotizacionesCar, layoutPedidosCar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
