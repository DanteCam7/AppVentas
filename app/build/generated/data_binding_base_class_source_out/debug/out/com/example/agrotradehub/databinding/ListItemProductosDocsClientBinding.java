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

public final class ListItemProductosDocsClientBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView fechaDocumento;

  @NonNull
  public final ImageView iconoApoyo;

  @NonNull
  public final TextView iddocumento;

  @NonNull
  public final TextView nombreAgente;

  @NonNull
  public final TextView totalDocumento;

  private ListItemProductosDocsClientBinding(@NonNull LinearLayout rootView,
      @NonNull TextView fechaDocumento, @NonNull ImageView iconoApoyo,
      @NonNull TextView iddocumento, @NonNull TextView nombreAgente,
      @NonNull TextView totalDocumento) {
    this.rootView = rootView;
    this.fechaDocumento = fechaDocumento;
    this.iconoApoyo = iconoApoyo;
    this.iddocumento = iddocumento;
    this.nombreAgente = nombreAgente;
    this.totalDocumento = totalDocumento;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ListItemProductosDocsClientBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListItemProductosDocsClientBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.list_item_productos_docs_client, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListItemProductosDocsClientBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.fechaDocumento;
      TextView fechaDocumento = ViewBindings.findChildViewById(rootView, id);
      if (fechaDocumento == null) {
        break missingId;
      }

      id = R.id.iconoApoyo;
      ImageView iconoApoyo = ViewBindings.findChildViewById(rootView, id);
      if (iconoApoyo == null) {
        break missingId;
      }

      id = R.id.iddocumento;
      TextView iddocumento = ViewBindings.findChildViewById(rootView, id);
      if (iddocumento == null) {
        break missingId;
      }

      id = R.id.nombreAgente;
      TextView nombreAgente = ViewBindings.findChildViewById(rootView, id);
      if (nombreAgente == null) {
        break missingId;
      }

      id = R.id.totalDocumento;
      TextView totalDocumento = ViewBindings.findChildViewById(rootView, id);
      if (totalDocumento == null) {
        break missingId;
      }

      return new ListItemProductosDocsClientBinding((LinearLayout) rootView, fechaDocumento,
          iconoApoyo, iddocumento, nombreAgente, totalDocumento);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}