// Generated by view binder compiler. Do not edit!
package com.example.agrotradehub.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView agrotradehub;

  @NonNull
  public final Button btnIniciarSesion;

  @NonNull
  public final TextInputEditText edtPassword;

  @NonNull
  public final TextInputEditText edtUser;

  @NonNull
  public final LinearLayout linearParte1;

  @NonNull
  public final LinearLayout linearParte2;

  @NonNull
  public final TextInputLayout txtInputPassword;

  @NonNull
  public final TextInputLayout txtInputUser;

  private ActivityLoginBinding(@NonNull RelativeLayout rootView, @NonNull ImageView agrotradehub,
      @NonNull Button btnIniciarSesion, @NonNull TextInputEditText edtPassword,
      @NonNull TextInputEditText edtUser, @NonNull LinearLayout linearParte1,
      @NonNull LinearLayout linearParte2, @NonNull TextInputLayout txtInputPassword,
      @NonNull TextInputLayout txtInputUser) {
    this.rootView = rootView;
    this.agrotradehub = agrotradehub;
    this.btnIniciarSesion = btnIniciarSesion;
    this.edtPassword = edtPassword;
    this.edtUser = edtUser;
    this.linearParte1 = linearParte1;
    this.linearParte2 = linearParte2;
    this.txtInputPassword = txtInputPassword;
    this.txtInputUser = txtInputUser;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.agrotradehub;
      ImageView agrotradehub = ViewBindings.findChildViewById(rootView, id);
      if (agrotradehub == null) {
        break missingId;
      }

      id = R.id.btnIniciarSesion;
      Button btnIniciarSesion = ViewBindings.findChildViewById(rootView, id);
      if (btnIniciarSesion == null) {
        break missingId;
      }

      id = R.id.edtPassword;
      TextInputEditText edtPassword = ViewBindings.findChildViewById(rootView, id);
      if (edtPassword == null) {
        break missingId;
      }

      id = R.id.edtUser;
      TextInputEditText edtUser = ViewBindings.findChildViewById(rootView, id);
      if (edtUser == null) {
        break missingId;
      }

      id = R.id.linearParte1;
      LinearLayout linearParte1 = ViewBindings.findChildViewById(rootView, id);
      if (linearParte1 == null) {
        break missingId;
      }

      id = R.id.linearParte2;
      LinearLayout linearParte2 = ViewBindings.findChildViewById(rootView, id);
      if (linearParte2 == null) {
        break missingId;
      }

      id = R.id.txtInputPassword;
      TextInputLayout txtInputPassword = ViewBindings.findChildViewById(rootView, id);
      if (txtInputPassword == null) {
        break missingId;
      }

      id = R.id.txtInputUser;
      TextInputLayout txtInputUser = ViewBindings.findChildViewById(rootView, id);
      if (txtInputUser == null) {
        break missingId;
      }

      return new ActivityLoginBinding((RelativeLayout) rootView, agrotradehub, btnIniciarSesion,
          edtPassword, edtUser, linearParte1, linearParte2, txtInputPassword, txtInputUser);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}