package com.example.duantotnghiep.Service;

import android.util.Log;

import com.example.duantotnghiep.Contract.OrderDetailContract;
import com.example.duantotnghiep.Model.OrderDetail;
import com.example.duantotnghiep.Model.Response.OrderDetailResponse;
import com.example.duantotnghiep.Model.Response.OrderResponse;
import com.example.duantotnghiep.Retrofit.ApiClient;
import com.example.duantotnghiep.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailService implements OrderDetailContract.OrderDetailModel {

    @Override
    public void onGetOrderDetail(OnOrderDetailFinishedListener orderDetailFinishedListener, int orderId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderDetailResponse> call = apiInterface.get_order_detail(orderId);
        call.enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d("Order Detail Service", "Response code : " + response.code() + "----" + response.body().getMessage());
                    orderDetailFinishedListener.onOrderDetailFinished(response.body());
                }
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                orderDetailFinishedListener.onOrderDetailFailure(t);
                Log.d("Order Detail Service", "Error : " + t.getMessage());
            }
        });

    }
}
