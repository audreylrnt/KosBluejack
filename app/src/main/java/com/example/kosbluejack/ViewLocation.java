package com.example.kosbluejack;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class ViewLocation extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        Intent getPosition = getIntent();
        String pos = getPosition.getStringExtra("position");
        int positionInArray = Integer.parseInt(pos);
        latitude = KostDataDB.kostData.get(positionInArray).getLatitude();
        longitude = KostDataDB.kostData.get(positionInArray).getLongitude();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng latLng = new LatLng(latitude, longitude);
//        Log.d("Maps", "latlng: " + latLng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        gMap.clear();
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        gMap.addMarker(markerOptions);
//        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
//                gMap.clear();
//                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                gMap.addMarker(markerOptions);
//            }
//        });
    }
}
