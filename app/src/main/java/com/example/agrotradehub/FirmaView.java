package com.example.agrotradehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Address;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class FirmaView extends AppCompatActivity {

    private ImageView imageView;
    private TextView tvLocation;
    private float floatStartX = -1, floatStartY = -1, floatEndX = -1, floatEndY = -1;
    private Bitmap bitmap;
    private Canvas canvas;
    private ProgressBar cargaLocalizacion;
    private Paint paint = new Paint();
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma_view);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        startLocationUpdates();
        cargaLocalizacion = findViewById(R.id.cargaLocalización);
        imageView = findViewById(R.id.imageView);
        tvLocation = findViewById(R.id.tvLocation);
        cargaLocalizacion.setVisibility(View.VISIBLE);
    }

    private void startLocationUpdates() {
        try {
            // Request location updates from the GPS provider (or NETWORK_PROVIDER)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Handle location changes here
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Example: Display latitude and longitude in a Toast
            Toast.makeText(FirmaView.this, "Latitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_SHORT).show();

            Geocoder geocoder = new Geocoder(FirmaView.this);
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);

                    // Get the address information (e.g., street, city, country)
                    String addressText = address.getAddressLine(0);

                    // Display the approximate address in a TextView or any other component
                    tvLocation.setVisibility(View.VISIBLE);
                    cargaLocalizacion.setVisibility(View.GONE);
                    tvLocation.setText("Approximate Address: " + addressText);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
    private void drawPaintSketchImage(){
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
                    Bitmap.Config.ARGB_8888);

            canvas = new Canvas(bitmap);

            paint.setColor(Color.RED);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
        }

        canvas.drawLine(floatStartX,
                floatStartY-220,
                floatEndX,
                floatEndY-220,
                paint);

        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            floatEndX = event.getX();
            floatEndY = event.getY();

            drawPaintSketchImage();

            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_UP){
            floatEndX = event.getX();
            floatEndY = event.getY();

            drawPaintSketchImage();
        }

        return super.onTouchEvent(event);
    }

    public void buttonSaveFirma(View view){
        if (cargaLocalizacion.getVisibility() == View.VISIBLE){
            Toast.makeText(FirmaView.this, "Se esta cargando la localización", Toast.LENGTH_SHORT).show();
        } else if (bitmap == null) {
            Toast.makeText(FirmaView.this, "No hay ninguna firma", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(FirmaView.this, "Salvaguardar", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonClearFirma(View view){
        if (bitmap == null){
            Toast.makeText(FirmaView.this, "No hay nada que borrar", Toast.LENGTH_SHORT).show();
        }else {
            bitmap = null;
            imageView.setImageBitmap(bitmap);
        }
    }
}