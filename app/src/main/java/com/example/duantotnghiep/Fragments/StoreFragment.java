package com.example.duantotnghiep.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.StoreAdapter;
import com.example.duantotnghiep.Contract.StoreContact;
import com.example.duantotnghiep.Model.Store;
import com.example.duantotnghiep.Presenter.StorePresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;


public class StoreFragment extends Fragment implements StoreContact.View {
    boolean isPermissionGranted;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment supportMapFragment;
    private StorePresenter storePresenter;
    private RecyclerView rcvStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvStore = view.findViewById(R.id.rcvStore);

        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationMain);
            rcvStore.setOnTouchListener(new TranslateAnimation(getActivity(), bottomNavigationView));

        }
        storePresenter = new StorePresenter(this);

        checkMyPermission();
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mvStore);
        if (isPermissionGranted) {
            storePresenter.getProduct();
        }
    }

    private void checkMyPermission() {
        Dexter.withContext(getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                storePresenter.getProduct();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), "");
                intent.setData(uri);
                getContext().startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public void setStore(List<Store> mListStore) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        @SuppressLint("MissingPermission")
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        StoreAdapter storeAdapter = new StoreAdapter(getContext(), mListStore, new StoreAdapter.OnClickListener() {
                            @Override
                            public void onClickListener(double latitude, double longitude) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 20));
                            }
                        });
                        rcvStore.setLayoutManager(new LinearLayoutManager(getContext()));
                        rcvStore.setAdapter(storeAdapter);
                        for (int i = 0; i < mListStore.size(); i++) {
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(mListStore.get(i).getLatitude(), mListStore.get(i).getLongitude())));
                        }
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onResponseFail(Throwable t) {
        Log.d("StoreFragment",t.getMessage());
    }
}