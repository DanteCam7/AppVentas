// Generated by view binder compiler. Do not edit!
package com.example.agrotradehub.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.agrotradehub.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentAddOneClientBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final Button botonEnviar;

  @NonNull
  public final TextInputEditText editTextCalle;

  @NonNull
  public final TextInputEditText editTextCiudad;

  @NonNull
  public final TextInputEditText editTextCodPostal;

  @NonNull
  public final Spinner editTextCodigoPais;

  @NonNull
  public final TextInputEditText editTextColonia;

  @NonNull
  public final TextInputEditText editTextCorreo;

  @NonNull
  public final TextInputEditText editTextEstado;

  @NonNull
  public final TextInputEditText editTextMunicipio;

  @NonNull
  public final TextInputEditText editTextNombre;

  @NonNull
  public final EditText editTextNumeroExterior;

  @NonNull
  public final TextInputEditText editTextNumeroInterior;

  @NonNull
  public final TextInputEditText editTextRFC;

  @NonNull
  public final TextInputEditText editTextTelefono;

  @NonNull
  public final TextInputEditText editTextTelefonoFijo;

  @NonNull
  public final TextInputLayout layoutCP;

  @NonNull
  public final TextInputLayout layouteditTextNombre;

  @NonNull
  public final Spinner spinnerMoneda;

  @NonNull
  public final Spinner spinnercodigoPais;

  private FragmentAddOneClientBinding(@NonNull FrameLayout rootView, @NonNull Button botonEnviar,
      @NonNull TextInputEditText editTextCalle, @NonNull TextInputEditText editTextCiudad,
      @NonNull TextInputEditText editTextCodPostal, @NonNull Spinner editTextCodigoPais,
      @NonNull TextInputEditText editTextColonia, @NonNull TextInputEditText editTextCorreo,
      @NonNull TextInputEditText editTextEstado, @NonNull TextInputEditText editTextMunicipio,
      @NonNull TextInputEditText editTextNombre, @NonNull EditText editTextNumeroExterior,
      @NonNull TextInputEditText editTextNumeroInterior, @NonNull TextInputEditText editTextRFC,
      @NonNull TextInputEditText editTextTelefono, @NonNull TextInputEditText editTextTelefonoFijo,
      @NonNull TextInputLayout layoutCP, @NonNull TextInputLayout layouteditTextNombre,
      @NonNull Spinner spinnerMoneda, @NonNull Spinner spinnercodigoPais) {
    this.rootView = rootView;
    this.botonEnviar = botonEnviar;
    this.editTextCalle = editTextCalle;
    this.editTextCiudad = editTextCiudad;
    this.editTextCodPostal = editTextCodPostal;
    this.editTextCodigoPais = editTextCodigoPais;
    this.editTextColonia = editTextColonia;
    this.editTextCorreo = editTextCorreo;
    this.editTextEstado = editTextEstado;
    this.editTextMunicipio = editTextMunicipio;
    this.editTextNombre = editTextNombre;
    this.editTextNumeroExterior = editTextNumeroExterior;
    this.editTextNumeroInterior = editTextNumeroInterior;
    this.editTextRFC = editTextRFC;
    this.editTextTelefono = editTextTelefono;
    this.editTextTelefonoFijo = editTextTelefonoFijo;
    this.layoutCP = layoutCP;
    this.layouteditTextNombre = layouteditTextNombre;
    this.spinnerMoneda = spinnerMoneda;
    this.spinnercodigoPais = spinnercodigoPais;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentAddOneClientBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentAddOneClientBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_add_one_client, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentAddOneClientBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.botonEnviar;
      Button botonEnviar = ViewBindings.findChildViewById(rootView, id);
      if (botonEnviar == null) {
        break missingId;
      }

      id = R.id.editTextCalle;
      TextInputEditText editTextCalle = ViewBindings.findChildViewById(rootView, id);
      if (editTextCalle == null) {
        break missingId;
      }

      id = R.id.editTextCiudad;
      TextInputEditText editTextCiudad = ViewBindings.findChildViewById(rootView, id);
      if (editTextCiudad == null) {
        break missingId;
      }

      id = R.id.editTextCodPostal;
      TextInputEditText editTextCodPostal = ViewBindings.findChildViewById(rootView, id);
      if (editTextCodPostal == null) {
        break missingId;
      }

      id = R.id.editTextCodigoPais;
      Spinner editTextCodigoPais = ViewBindings.findChildViewById(rootView, id);
      if (editTextCodigoPais == null) {
        break missingId;
      }

      id = R.id.editTextColonia;
      TextInputEditText editTextColonia = ViewBindings.findChildViewById(rootView, id);
      if (editTextColonia == null) {
        break missingId;
      }

      id = R.id.editTextCorreo;
      TextInputEditText editTextCorreo = ViewBindings.findChildViewById(rootView, id);
      if (editTextCorreo == null) {
        break missingId;
      }

      id = R.id.editTextEstado;
      TextInputEditText editTextEstado = ViewBindings.findChildViewById(rootView, id);
      if (editTextEstado == null) {
        break missingId;
      }

      id = R.id.editTextMunicipio;
      TextInputEditText editTextMunicipio = ViewBindings.findChildViewById(rootView, id);
      if (editTextMunicipio == null) {
        break missingId;
      }

      id = R.id.editTextNombre;
      TextInputEditText editTextNombre = ViewBindings.findChildViewById(rootView, id);
      if (editTextNombre == null) {
        break missingId;
      }

      id = R.id.editTextNumeroExterior;
      EditText editTextNumeroExterior = ViewBindings.findChildViewById(rootView, id);
      if (editTextNumeroExterior == null) {
        break missingId;
      }

      id = R.id.editTextNumeroInterior;
      TextInputEditText editTextNumeroInterior = ViewBindings.findChildViewById(rootView, id);
      if (editTextNumeroInterior == null) {
        break missingId;
      }

      id = R.id.editTextRFC;
      TextInputEditText editTextRFC = ViewBindings.findChildViewById(rootView, id);
      if (editTextRFC == null) {
        break missingId;
      }

      id = R.id.editTextTelefono;
      TextInputEditText editTextTelefono = ViewBindings.findChildViewById(rootView, id);
      if (editTextTelefono == null) {
        break missingId;
      }

      id = R.id.editTextTelefonoFijo;
      TextInputEditText editTextTelefonoFijo = ViewBindings.findChildViewById(rootView, id);
      if (editTextTelefonoFijo == null) {
        break missingId;
      }

      id = R.id.layoutCP;
      TextInputLayout layoutCP = ViewBindings.findChildViewById(rootView, id);
      if (layoutCP == null) {
        break missingId;
      }

      id = R.id.layouteditTextNombre;
      TextInputLayout layouteditTextNombre = ViewBindings.findChildViewById(rootView, id);
      if (layouteditTextNombre == null) {
        break missingId;
      }

      id = R.id.spinnerMoneda;
      Spinner spinnerMoneda = ViewBindings.findChildViewById(rootView, id);
      if (spinnerMoneda == null) {
        break missingId;
      }

      id = R.id.spinnercodigoPais;
      Spinner spinnercodigoPais = ViewBindings.findChildViewById(rootView, id);
      if (spinnercodigoPais == null) {
        break missingId;
      }

      return new FragmentAddOneClientBinding((FrameLayout) rootView, botonEnviar, editTextCalle,
          editTextCiudad, editTextCodPostal, editTextCodigoPais, editTextColonia, editTextCorreo,
          editTextEstado, editTextMunicipio, editTextNombre, editTextNumeroExterior,
          editTextNumeroInterior, editTextRFC, editTextTelefono, editTextTelefonoFijo, layoutCP,
          layouteditTextNombre, spinnerMoneda, spinnercodigoPais);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}