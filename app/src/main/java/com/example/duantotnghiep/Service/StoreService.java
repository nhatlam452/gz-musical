package com.example.duantotnghiep.Service;


import android.util.Log;

import com.example.duantotnghiep.Contract.StoreContact;
import com.example.duantotnghiep.Model.Response.StoreResponse;
import com.example.duantotnghiep.Retrofit.ApiClient;
import com.example.duantotnghiep.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreService implements StoreContact.StoreModel {
    private final String TAG ="Store Service";
    @Override
    public void getAllProduct(OnStoreListener onStoreListener) {
        Call<StoreResponse> call = ApiClient.getClient().create(ApiInterface.class).get_all_store();
        call.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
                    onStoreListener.onStoreFinished(response.body());
                }
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                onStoreListener.onStoreFailure(t);
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });

    }
}
