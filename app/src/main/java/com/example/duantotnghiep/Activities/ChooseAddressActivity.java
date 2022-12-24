package com.example.duantotnghiep.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.ChooseAddressAdapter;
import com.example.duantotnghiep.Contract.AddressContact;
import com.example.duantotnghiep.Fragments.CartFragment;
import com.example.duantotnghiep.Model.UserAddress;
import com.example.duantotnghiep.Presenter.AddressPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppConstants;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseAddressActivity extends AppCompatActivity implements AddressContact.View {
    private AddressPresenter addressPresenter;
    private RecyclerView rcvChooseAddress;
    private AutoCompleteTextView edtSearchAddress;
    private SearchView sv;
    private  SharedPreferences mSharePrefer = null;
    private SharedPreferences.Editor mEditor;
    private List<AutocompletePrediction> predictionList;
    private PlacesClient placesClient;
    private final ActivityResultLauncher<Intent> launchChooseAddress = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = new Intent();
                intent.putExtra("addressName", "Delivery To");
                intent.putExtra("address", result.getData().getStringExtra("address"));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        mSharePrefer = getSharedPreferences(AppConstants.CHECK_PERMISSION, 0);
        mEditor = mSharePrefer.edit();
        addressPresenter = new AddressPresenter(this);
        rcvChooseAddress = findViewById(R.id.rcvChooseAddress);
        edtSearchAddress = findViewById(R.id.edtSearchAddress);
        addressPresenter.onGetAddress(LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo().getUserId());
        Places.initialize(this, "AIzaSyBiebu4Qm95Q67dubdDb2Bn6XqGnliFXB4");
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        edtSearchAddress.setHint(getIntent().getStringExtra("addressHint"));
        edtSearchAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setCountry("vn")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {

                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++) {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionList.add(prediction.getFullText(null).toString());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(ChooseAddressActivity.this, android.R.layout.simple_list_item_1, suggestionList);
                                edtSearchAddress.setAdapter(adapter);
                            }
                        } else {
                            Log.d("ChooseAddressActivity", task.getException().getMessage() + " prediction fetching task unsuccessful");
                        }
                    }
                });


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.imgBackChooseAddress).setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        findViewById(R.id.tvYourLocation).setOnClickListener(v -> getCurrentLocation());
        findViewById(R.id.tvChooseFromMaps).setOnClickListener(v -> chooseFromMap());

    }
    /**
     * Start the ChooseFromMapActivity.
     */
    private void chooseFromMap() {
        // Check if the app has the location permission
        if (hasLocationPermission()) {
            // Set the write permission request flag to true
            setLocationPermissionRequest(true);
        }

        // Check if the app has the location permission or has requested the permission once
        if (hasLocationPermission() || hasLocationPermissionOnetime()) {
            // Start the ChooseFromMapActivity
            launchChooseAddress.launch(new Intent(this, ChooseFromMapActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        } else {
            // Request the location permission
            checkMyPermission(false);
        }
    }

    /**
     * Finish the current activity and return the result to the caller.
     */
    private void getCurrentLocation() {
        // Check if the app has the location permission
        if (hasLocationPermission()) {
            // Set the write permission request flag to true
            setLocationPermissionRequest(true);
        }

        // Check if the app has the location permission or has requested the permission once
        if (hasLocationPermission() || hasLocationPermissionOnetime()) {
            // Finish the current activity and return the result to the caller
            Intent intent = new Intent();
            intent.putExtra("currentLocation", true);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            // Request the location permission
            checkMyPermission(true);
        }
    }

    /**
     * Check if the app has the location permission.
     *
     * @return true if the app has the location permission, false otherwise
     */
    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check if the app has requested the location permission once.
     *
     * @return true if the app has requested the location permission once, false otherwise
     */
    private boolean hasLocationPermissionOnetime() {
        return mSharePrefer.getBoolean(AppConstants.iSLocationPermissionRequestOnetime, false);
    }

    /**
     * Set the write permission request flag in the shared preferences.
     *
     * @param value the value of the flag
     */
    private void setLocationPermissionRequest(boolean value) {
        mEditor.putBoolean(AppConstants.iSLocationPermissionRequest, value);
        mEditor.apply();
    }

    private void checkMyPermission(boolean isCurrent) {

        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                mEditor.putBoolean(AppConstants.iSLocationPermissionRequest, true);
                mEditor.putBoolean(AppConstants.iSLocationPermissionRequestOnetime, true);
                mEditor.apply();
                if (isCurrent) {
                    Intent intent = new Intent();
                    intent.putExtra("currentLocation", true);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent i = new Intent(ChooseAddressActivity.this, ChooseFromMapActivity.class);
                    launchChooseAddress.launch(i);
                    overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
                }

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(ChooseAddressActivity.this, "Please Granted Permission in your Setting", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onGetAddressSuccess(List<UserAddress> userAddressList) {
        ChooseAddressAdapter chooseAddressAdapter = new ChooseAddressAdapter(this, userAddressList, new ChooseAddressAdapter.OnClickListener() {
            @Override
            public void onClickListener(String addressName, String address, String ward, String district, String city) {
                Intent intent = new Intent();
                String rs = address + " " + ward + " " + district + " " + city;
                intent.putExtra("addressName", addressName);
                intent.putExtra("address", rs);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rcvChooseAddress.setLayoutManager(new LinearLayoutManager(this));
        rcvChooseAddress.setAdapter(chooseAddressAdapter);

    }

    @Override
    public void onGetAddressFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetAddressResponseFail(Throwable t) {
        Toast.makeText(this, "Unknown Error. Please check your connection", Toast.LENGTH_SHORT).show();
        Log.d("ChooseAddressActivity", "Error : " + t.getMessage());

    }
}