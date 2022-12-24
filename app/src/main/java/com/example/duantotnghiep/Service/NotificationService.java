package com.example.duantotnghiep.Service;

import android.util.Log;

import com.example.duantotnghiep.Contract.NotificationContact;
import com.example.duantotnghiep.Model.Response.NotificationResponse;
import com.example.duantotnghiep.Retrofit.ApiClient;
import com.example.duantotnghiep.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService implements NotificationContact.NotificationModel {

    @Override
    public void sendNotification(OnNotificationFinishedListener notificationFinishedListener, String userId, String notification, String title, String timeSent) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NotificationResponse> call = apiInterface.send_notification(userId, notification, title, timeSent);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d("Notification Service", "Response code : " + response.code() + "----" + response.body().getMessage());
                    notificationFinishedListener.onNotificationFinished(response.body());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                notificationFinishedListener.onNotificationFailure(t);
                Log.d("Notification Service", "Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void getNotification(OnNotificationFinishedListener notificationFinishedListener, String userId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NotificationResponse> call = apiInterface.get_notification(userId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d("Notification Service", "Response code : " + response.code() + "----" + response.body().getMessage());
                    notificationFinishedListener.onNotificationFinished(response.body());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                notificationFinishedListener.onNotificationFailure(t);
                Log.d("Notification Service", "Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void setNotificationView(OnNotificationFinishedListener notificationView, String userId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NotificationResponse> call = apiInterface.set_notification_viewed(userId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d("Notification Service", "Response code : " + response.code() + "----" + response.body().getMessage());
                    notificationView.onNotificationFinished(response.body());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                notificationView.onNotificationFailure(t);
                Log.d("Notification Service", "Error : " + t.getMessage());
            }
        });
    }
}


