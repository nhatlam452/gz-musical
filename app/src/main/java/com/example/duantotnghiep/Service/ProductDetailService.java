package com.example.duantotnghiep.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.duantotnghiep.Contract.DetailContract;
import com.example.duantotnghiep.Model.Response.ProductDetailResponse;
import com.example.duantotnghiep.Retrofit.ApiClient;
import com.example.duantotnghiep.Retrofit.ApiInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailService implements DetailContract.DetailModel {
    private final String TAG = "GET_PRODUCT_DETAIL";

    @Override
    public void getProductDetail(OnGetDetailFinished onGetDetailFinished, int productId) {
         ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductDetailResponse> call = apiInterface.get_product_detail(productId);
        call.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductDetailResponse> call, @NonNull Response<ProductDetailResponse> response) {
                if (response.isSuccessful()){
                    onGetDetailFinished.onGetDetailSuccess(response.body());
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.d(TAG,json+"");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductDetailResponse> call, @NonNull Throwable t) {
                Log.d(TAG,t.getMessage());
                onGetDetailFinished.onGetDetailFailure(t);
            }
        });

    }
}
