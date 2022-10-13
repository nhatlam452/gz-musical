package com.example.duantotnghiep.Retrofit;

import android.content.Context;


import com.example.duantotnghiep.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

        private static final String BASE_URL = String.valueOf(R.string.BASE_URL_API);
        private static Retrofit retrofit = null;
        public static Retrofit getClient(){
            if (retrofit == null){
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }










}


