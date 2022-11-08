package com.example.duantotnghiep.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.duantotnghiep.Contract.CartContact;
import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.Response.CartResponse;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Retrofit.ApiClient;
import com.example.duantotnghiep.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartService implements CartContact.CartModel {
    private final String TAG = "CART_SERVICE";
    private final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void addToCart(OnCartFinishedListener onCartFinishedListener, int userId, int productId, int quantity) {
        Call<CartResponse> call = apiInterface.add_to_cart(userId,productId,quantity);
        onCartCallback(call,onCartFinishedListener);
    }

    @Override
    public void getAllCart(OnCartFinishedListener onCartFinishedListener, int userId) {
        Call<CartResponse> call = apiInterface.get_all_cart(userId);
        onCartCallback(call,onCartFinishedListener);

    }

    private void onCartCallback (Call < CartResponse > call, OnCartFinishedListener onFinished){
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
                    onFinished.onCartFinished(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                onFinished.onCartFailure(t);
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });
    }
}
