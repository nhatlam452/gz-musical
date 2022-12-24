package com.example.duantotnghiep.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.StoreAdapter;
import com.example.duantotnghiep.Contract.StoreContact;
import com.example.duantotnghiep.Model.Store;
import com.example.duantotnghiep.Presenter.StorePresenter;
import com.example.duantotnghiep.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class ChooseStoreActivity extends AppCompatActivity implements StoreContact.View {
    // Declare and initialize variables
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;
    private StorePresenter storePresenter;
    private RecyclerView rcvStore;

    // Set up a result launcher to handle the result of the StoreInfoActivity
    private final ActivityResultLauncher<Intent> launchChooseStore = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = new Intent();
                // Put the store name and address in the intent as extras
                intent.putExtra("storeInfoName", result.getData().getStringExtra("storeInfoName"));
                intent.putExtra("storeInfoAddress", result.getData().getStringExtra("storeInfoAddress"));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_store);
        rcvStore = findViewById(R.id.rcvChooseStore);
        storePresenter = new StorePresenter(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mvChooseStore);
        storePresenter.getProduct();
    }

    @Override
    public void setStore(List<Store> mListStore) {
        // Get the last known location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("MissingPermission")
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Load the map
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                        // Set up the store adapter and attach it to the recycler view
                        StoreAdapter storeAdapter = new StoreAdapter(ChooseStoreActivity.this, mListStore, new StoreAdapter.OnClickListener() {
                            @Override
                            public void onCLickChooseStore(String storeName, String storeAddress) {
                                Intent i = new Intent(ChooseStoreActivity.this, StoreInfoActivity.class);
                                i.putExtra("storeName", storeName);
                                i.putExtra("storeAddress", storeAddress);
                                // Start the StoreInfoActivity and handle the result with the result launcher
                                launchChooseStore.launch(i);
                                overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
                            }

                            @Override
                            public void onClickListener(double latitude, double longitude) {
                                // Animate the camera to the selected store's location
                                LatLng latLng = new LatLng(latitude, longitude);
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                                googleMap.animateCamera(cameraUpdate);
                            }

                            @Override
                            public void onGetDistance(TextView textView, double latitude, double longitude) {
                                // Calculate the distance between the current location and the selected store
                                float[] result = new float[10];
                                Location.distanceBetween(location.getLatitude(), location.getLongitude(), latitude, longitude, result);
                                // Format the distance and set it as the text of the text view
                                String s = String.format("%.1f", result[0] / 1000);
                                textView.setText(s + " " + getResources().getString(R.string.km_away_from_you));
                            }
                        });

                        rcvStore.setLayoutManager(new LinearLayoutManager(ChooseStoreActivity.this));
                        rcvStore.setAdapter(storeAdapter);

                        // Add markers for each store to the map
                        for (int i = 0; i < mListStore.size(); i++) {
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(mListStore.get(i).getLatitude(), mListStore.get(i).getLongitude())).title(mListStore.get(i).getStoreName()));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onResponseFail(Throwable t) {
        Toast.makeText(this, "Unknown Error. Please check your connection", Toast.LENGTH_SHORT).show();
        Log.d("ChooseStoreActivity", t.getMessage());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Set a custom animation for when the activity finishes
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }
}
