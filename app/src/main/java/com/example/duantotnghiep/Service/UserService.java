package com.example.duantotnghiep.Service;


import android.util.Log;

import com.example.duantotnghiep.Contract.LoginContract;
import com.example.duantotnghiep.Contract.RegisterContract;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Retrofit.ApiInterface;
import com.example.duantotnghiep.Retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService implements LoginContract.Model, RegisterContract.Model {
    private  ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private final String TAG = "User Service :";
    @Override
    public void getLogin(OnFinishedListener onFinishedListener,String phone , String password) {

        Call<UserResponse> call = apiInterface.check_login(phone,password);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()){
                    UserResponse userResponse = response.body();
                    onFinishedListener.onLoginFinished(userResponse);
                    Log.d(TAG,"Success ");
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d(TAG,t.getMessage());
                onFinishedListener.onLoginFailure(t);
            }
        });

    }


    @Override
    public void getRegister(OnFinishedRegisterListener onFinishedListener, User user) {
        Call<UserResponse> call = apiInterface.register_user(user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()){
                    Log.d(TAG,"Register Succes");
                    onFinishedListener.onRegisterFinished(response.body().getResponseCode(),response.body().getMessage());
                }
                Log.d(TAG,"Response code : " +response.code() +"----"+ response.body().getMessage());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                    onFinishedListener.onRegisterFailure(t);
                    Log.d(TAG,t.getMessage());
            }
        });

    }
}
