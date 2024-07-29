package com.example.agrotradehub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agrotradehub.R;
import com.example.agrotradehub.models.CountryInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<CountryInfo> {
    public SpinnerAdapter(@NonNull Context context, List<CountryInfo> countrys) {
        super(context, R.layout.custom_spinner_item, countrys);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CountryInfo countryInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item, parent, false);
        }

        TextView txtName = convertView.findViewById(R.id.countryName);
        TextView txtCodigo = convertView.findViewById(R.id.countryCode);
        ImageView imvBandera = convertView.findViewById(R.id.countryIcon);

        txtName.setText(" ("+countryInfo.getSiglas()+") ");
        txtCodigo.setVisibility(View.VISIBLE);
        txtCodigo.setText("+"+countryInfo.getPhone());
        Picasso.get().load(countryInfo.getUrlBandera()).into(imvBandera);
        return convertView;
    }

    private View createCustomView(int position, View convertView, ViewGroup parent) {
        CountryInfo countryInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item, parent, false);
        }

        TextView txtCodigo = convertView.findViewById(R.id.countryName);
        ImageView imvBandera = convertView.findViewById(R.id.countryIcon);

        txtCodigo.setText(countryInfo.getSiglas());
        Picasso.get().load(countryInfo.getUrlBandera()).into(imvBandera);
        return convertView;
    }
}
