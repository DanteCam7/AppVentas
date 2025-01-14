// Generated by view binder compiler. Do not edit!
package com.example.agrotradehub.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.agrotradehub.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDocumentosBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final Spinner clienteDoc;

  @NonNull
  public final ProgressBar docsCarga;

  @NonNull
  public final TextView fechaInicio;

  @NonNull
  public final TextView fechaTermino;

  @NonNull
  public final ListView listadoDocumentos;

  @NonNull
  public final TextView noDocs;

  private FragmentDocumentosBinding(@NonNull FrameLayout rootView, @NonNull Spinner clienteDoc,
      @NonNull ProgressBar docsCarga, @NonNull TextView fechaInicio, @NonNull TextView fechaTermino,
      @NonNull ListView listadoDocumentos, @NonNull TextView noDocs) {
    this.rootView = rootView;
    this.clienteDoc = clienteDoc;
    this.docsCarga = docsCarga;
    this.fechaInicio = fechaInicio;
    this.fechaTermino = fechaTermino;
    this.listadoDocumentos = listadoDocumentos;
    this.noDocs = noDocs;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDocumentosBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDocumentosBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_documentos, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDocumentosBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.clienteDoc;
      Spinner clienteDoc = ViewBindings.findChildViewById(rootView, id);
      if (clienteDoc == null) {
        break missingId;
      }

      id = R.id.docsCarga;
      ProgressBar docsCarga = ViewBindings.findChildViewById(rootView, id);
      if (docsCarga == null) {
        break missingId;
      }

      id = R.id.fechaInicio;
      TextView fechaInicio = ViewBindings.findChildViewById(rootView, id);
      if (fechaInicio == null) {
        break missingId;
      }

      id = R.id.fechaTermino;
      TextView fechaTermino = ViewBindings.findChildViewById(rootView, id);
      if (fechaTermino == null) {
        break missingId;
      }

      id = R.id.listadoDocumentos;
      ListView listadoDocumentos = ViewBindings.findChildViewById(rootView, id);
      if (listadoDocumentos == null) {
        break missingId;
      }

      id = R.id.noDocs;
      TextView noDocs = ViewBindings.findChildViewById(rootView, id);
      if (noDocs == null) {
        break missingId;
      }

      return new FragmentDocumentosBinding((FrameLayout) rootView, clienteDoc, docsCarga,
          fechaInicio, fechaTermino, listadoDocumentos, noDocs);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
