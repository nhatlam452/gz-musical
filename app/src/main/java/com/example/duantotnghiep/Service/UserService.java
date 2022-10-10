package com.example.duantotnghiep.Service;


import android.util.Log;

import com.example.duantotnghiep.Contract.LoginContract;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Retrofit.ApiInterface;
import com.example.duantotnghiep.Retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService implements LoginContract.Model {

    private final String TAG = "User Service :";
    @Override
    public void getLogin(OnFinishedListener onFinishedListener,String phone , String password) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<UserResponse> call = apiInterface.check_login(phone,password);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()){
                    UserResponse userResponse = response.body();
                    onFinishedListener.onFinished(userResponse);
                    Log.d(TAG,"Success ");
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d(TAG,t.getMessage());
                onFinishedListener.onFailure(t);
            }
        });

    }
}
