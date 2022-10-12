package com.example.duantotnghiep.Retrofit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Activities.LoginActivity;
import com.example.duantotnghiep.Activities.MainActivity;
import com.example.duantotnghiep.Adapter.ProductsAdapter;
import com.example.duantotnghiep.Model.Response.ProductResponse;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallback {
    public static Callback<UserResponse> getCheckLogin(Context context) {
        Callback<UserResponse> check_loginCallback = new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse.getResponseCode() == 0) {
                        Log.d("========>",userResponse.getMessage()+"");
                    } else if (userResponse.getResponseCode() == 1) {
//                        AppHelper.AppCheck.getInstance(context).getLocalStorageManager().setUserLogin(true);
                        Log.d("========>",userResponse.getMessage()+"");
                        Intent intent = new Intent(context, MainActivity.class);
                        ((LoginActivity) context).finishAffinity();
                        context.startActivity(intent);
                        ((LoginActivity) context).overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
                    } else {
                        Log.d("========>",userResponse.getMessage()+"");

                    }
                } else {
                    Log.d("======>", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("========>",t.getMessage()+"");

            }
        };
        return check_loginCallback;
    }

    public static Callback<ProductResponse> getAllProduct(Context context, RecyclerView recyclerView) {
        Callback<ProductResponse> getAllProductCallBack = new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse.getResponseCode() == 1) {
                        ProductsAdapter adapter = new ProductsAdapter(context,productResponse.getData());
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adapter);

                    }
                }
                ProductResponse productResponse = response.body();

                Log.d("===>>", "" + productResponse.getResponseCode());

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("========>",t.getMessage()+"");

            }
        };
        return getAllProductCallBack;
    }
}
