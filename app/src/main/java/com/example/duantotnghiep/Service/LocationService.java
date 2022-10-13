package com.example.duantotnghiep.Service;

import android.util.Log;
import android.widget.EditText;

import com.example.duantotnghiep.Contract.LocationContract;
import com.example.duantotnghiep.Model.Response.LocationResponse;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationService implements LocationContract.Model {
    private final String TAG_LOCATION = "GET_LOCATION_API";
    private final ApiInterface apiInterface = new Retrofit.Builder()
            .baseUrl("https://api.mysupership.vn/v1/partner/areas/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface.class);

    @Override
    public void getCityFromApi(OnFinishedListener onFinishedListener, EditText editText,String string) {
        Call<LocationResponse> call = apiInterface.getCity();
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()) {
                    LocationResponse locationResponse = response.body();
                    onFinishedListener.onFinished(locationResponse,editText,string);
                    Log.d(TAG_LOCATION, "GET CITY SUCCESS");
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.d(TAG_LOCATION, t.getMessage());
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void getDistrictFromApi(OnFinishedListener onFinishedListener, String code, EditText editText,String string) {
        Call<LocationResponse> call = apiInterface.getDistrict(code);

        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()) {
                    LocationResponse locationResponse = response.body();
                    onFinishedListener.onFinished(locationResponse,editText,string);
                    Log.d(TAG_LOCATION, "GET DISTRICT SUCCESS");
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.d(TAG_LOCATION, t.getMessage());
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void getWardFromApi(OnFinishedListener onFinishedListener, String code, EditText editText,String string) {
        Call<LocationResponse> call = apiInterface.getWard(code);

        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()) {
                    LocationResponse locationResponse = response.body();
                    onFinishedListener.onFinished(locationResponse,editText,string);
                    Log.d(TAG_LOCATION, "GET WARD SUCCESS");
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.d(TAG_LOCATION, t.getMessage());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
