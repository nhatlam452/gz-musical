package com.example.duantotnghiep.Service;

import android.util.Log;

import com.example.duantotnghiep.Contract.OrderContract;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.Response.OrderResponse;
import com.example.duantotnghiep.Retrofit.ApiClient;
import com.example.duantotnghiep.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService implements OrderContract.OrderModel {
    @Override
    public void newOrder(OnOrderFinishedListener orderFinishedListener, Order order) {
          ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderResponse> call = apiInterface.new_order(order);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d("Order Service", "Response code : " + response.code() + "----" + response.body().getMessage());
                    orderFinishedListener.onOrderFinished(response.body());
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                orderFinishedListener.onOrderFailure(t);
                Log.d("Order Service", "Error : " + t.getMessage());
            }
        });


    }

    @Override
    public void getOrder(OnOrderFinishedListener orderFinishedListener, int userId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderResponse> call = apiInterface.get_all_order(userId);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d("Order Service", "Response code : " + response.code() + "----" + response.body().getMessage());
                    orderFinishedListener.onOrderFinished(response.body());
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                orderFinishedListener.onOrderFailure(t);
                Log.d("Order Service", "Error : " + t.getMessage());
            }
        });
    }
    @Override
    public void cancelOrder(OnCancelOrderListener cancelOrderListener, int status, int orderId,String problem) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderResponse> call = apiInterface.update_order(status, orderId,problem);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d("Order Service", "Response code : " + response.code() + "----" + response.body().getMessage());
                    cancelOrderListener.onCancelOrderSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                cancelOrderListener.onCancelOrderFailure(t);
                Log.d("Order Service", "Error : " + t.getMessage());
            }
        });
    }
}

