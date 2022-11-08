package com.example.duantotnghiep.Service;


import android.util.Log;


import androidx.annotation.NonNull;

import com.example.duantotnghiep.Contract.AddressContact;
import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.Response.AddressResponse;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Model.UserAddress;
import com.example.duantotnghiep.Retrofit.ApiInterface;
import com.example.duantotnghiep.Retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService implements UserContract.Model, AddressContact.AddressModel {
    private final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private final String TAG = "User Service :";

    @Override
    public void getLogin(OnFinishedListener onFinishedListener, String phone, String password) {

        Call<UserResponse> call = apiInterface.check_login(phone, password);
        onUserCallback(call, onFinishedListener);
    }

    @Override
    public void getRegister(OnFinishedListener onFinishedListener, User user) {
        Call<UserResponse> call = apiInterface.register_user(user);
        onUserCallback(call, onFinishedListener);
    }

    @Override
    public void getSocialRegister(OnFinishedListener onFinishedListener, User user) {
        Call<UserResponse> call = apiInterface.social_login(user);
        onUserCallback(call,onFinishedListener);
    }

    @Override
    public void checkExitsUser(OnFinishedListener onFinishedListener, String phone) {
        Call<UserResponse> call = apiInterface.check_user_exits(phone);
        onUserCallback(call, onFinishedListener);
    }

    @Override
    public void changePassword(OnFinishedListener onFinishedListener, String phone, String newPassword, String password) {
        Call<UserResponse> call = apiInterface.change_user_password(phone, newPassword, null);
        onUserCallback(call, onFinishedListener);
    }

    @Override
    public void updateUser(OnFinishedListener onFinishedListener, String param, String value, String userId) {
        Call<UserResponse> call = apiInterface.update_info(param, value, userId);
        onUserCallback(call, onFinishedListener);
    }
    @Override
    public void getUserAddress(OnAddressFinished onAddressFinished, String userId) {
        Call<AddressResponse> call = apiInterface.get_user_address(userId);
        onAddressCallback(call,onAddressFinished);
    }

    @Override
    public void addAddress(OnAddressFinished onAddressFinished, UserAddress userAddress) {
        Call<AddressResponse> call = apiInterface.insert_address(userAddress);
        onAddressCallback(call,onAddressFinished);
    }
    private void onAddressCallback(Call<AddressResponse> call , OnAddressFinished onAddressFinished){
        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddressResponse> call, @NonNull Response<AddressResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
                    onAddressFinished.onAddressSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddressResponse> call, @NonNull Throwable t) {
                onAddressFinished.onAddressFailure(t);
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });
    }
    private void onUserCallback(Call<UserResponse> call, OnFinishedListener onFinishedListener) {
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
                    onFinishedListener.onFinished(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });
    }



}
