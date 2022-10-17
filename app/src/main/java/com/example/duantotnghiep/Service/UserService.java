package com.example.duantotnghiep.Service;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Retrofit.ApiInterface;
import com.example.duantotnghiep.Retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService implements UserContract.Model {
    private final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private final String TAG = "User Service :";

    @Override
    public void getLogin(OnFinishedListener onFinishedListener, String phone, String password) {

        Call<UserResponse> call = apiInterface.check_login(phone, password);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    onFinishedListener.onFinished(userResponse);
                }
                Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "Error : " + t.getMessage());
                onFinishedListener.onFailure(t);
            }
        });

    }

    @Override
    public void getRegister(OnFinishedListener onFinishedListener, User user) {
        Call<UserResponse> call = apiInterface.register_user(user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, "Register Succes");
                    onFinishedListener.onFinished(response.body());
                }

                Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void checkExitsUser(OnFinishedListener onFinishedListener, String phone) {
        Call<UserResponse> call = apiInterface.check_user_exits(phone);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
                    onFinishedListener.onFinished(response.body());
                }
                Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void changePassword(OnFinishedListener onFinishedListener, String phone, String newPassword, String password) {
        Call<UserResponse> call = apiInterface.change_user_password(phone, newPassword, null);
        Log.d(TAG,phone + "  " + newPassword);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
                    onFinishedListener.onFinished(response.body());
                }
                Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });

    }

}
