package com.example.duantotnghiep.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.StoreAdapter;
import com.example.duantotnghiep.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChooseFromMapActivity extends AppCompatActivity {
    // Declare and initialize variables
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;
    private TextView tvCFM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_from_map);
        tvCFM = findViewById(R.id.tvCFM);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mvCFM);

        // Get the last known location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("MissingPermission")
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Reverse geocode the location to get the address
                    Geocoder geocoder = new Geocoder(ChooseFromMapActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        tvCFM.setText(addresses.get(0).getAddressLine(0));
                        // Load the map and set the location and zoom level
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 25);
                                googleMap.animateCamera(cameraUpdate);
                                googleMap.setMyLocationEnabled(true);
                                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                                // Update the address when the camera position changes
                                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                                    @Override
                                    public void onCameraIdle() {
                                        Geocoder geocoder = new Geocoder(ChooseFromMapActivity.this, Locale.getDefault());
                                        List<Address> addresses = null;
                                        try {
                                            addresses = geocoder.getFromLocation(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, 1);
                                            tvCFM.setText(addresses.get(0).getAddressLine(0));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Show a toast message if there is an error getting the location
                Toast.makeText(ChooseFromMapActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set up a click listener for the back button
        findViewById(R.id.imgBackCFM).setOnClickListener(v -> onBackPressed());

        // Set up a click listener for the confirm button
        findViewById(R.id.btnConfirmCFM).setOnClickListener(v -> {
            Intent intent = new Intent();
            // Put the address in the intent as an extra
            intent.putExtra("address", tvCFM.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Set a custom animation for when the activity finishes
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }

}