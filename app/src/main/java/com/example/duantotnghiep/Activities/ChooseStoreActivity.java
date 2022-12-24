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
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;
    private StorePresenter storePresenter;
    private RecyclerView rcvStore;
    private ActivityResultLauncher<Intent> launchChooseStore = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                Intent intent = new Intent();
                intent.putExtra("storeInfoName",result.getData().getStringExtra("storeInfoName"));
                intent.putExtra("storeInfoAddress",result.getData().getStringExtra("storeInfoAddress"));
                setResult(RESULT_OK,intent);
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("MissingPermission")
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                        StoreAdapter storeAdapter = new StoreAdapter(ChooseStoreActivity.this, mListStore, new StoreAdapter.OnClickListener() {
                            @Override
                            public void onCLickChooseStore(String storeName, String storeAddress) {
                                Intent i = new Intent(ChooseStoreActivity.this,StoreInfoActivity.class);
                                i.putExtra("storeName",storeName);
                                i.putExtra("storeAddress",storeAddress);
                                launchChooseStore.launch(i);
                                overridePendingTransition(R.anim.anim_fadein,R.anim.anim_fadeout);
                            }

                            @Override
                            public void onClickListener(double latitude, double longitude) {
                                LatLng latLng = new LatLng(latitude, longitude);
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
                                googleMap.animateCamera(cameraUpdate);
                            }

                            @Override
                            public void onGetDistance(TextView textView, double latitude, double longitude) {
                                float[] result = new float[10];
                                Location.distanceBetween(location.getLatitude(), location.getLongitude(),latitude,longitude,result);
                                String s = String.format("%.1f",result[0] / 1000);
                                textView.setText(s +" "+ getResources().getString(R.string.km_away_from_you));
                            }


                        });

                        rcvStore.setLayoutManager(new LinearLayoutManager(ChooseStoreActivity.this));
                        rcvStore.setAdapter(storeAdapter);
                        for (int i = 0; i < mListStore.size(); i++) {
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(mListStore.get(i).getLatitude(), mListStore.get(i).getLongitude())));
                        }
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
                        googleMap.animateCamera(cameraUpdate);


                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChooseStoreActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResponseFail(Throwable t) {
        Toast.makeText(this, "Unknown Error. Please check your connection", Toast.LENGTH_SHORT).show();
        Log.d("ChooseStoreActivity", t.getMessage());
    }
}