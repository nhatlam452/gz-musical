package com.example.duantotnghiep.Retrofit;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static final String BASE_URL = "https://gzmusical.000webhostapp.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        // Create an OkHttpClient object
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // Add an interceptor to add a custom header to the request
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .header("GzMusicalNguyenNhatLamHeader", "GzMusicalNguyenNhatLamHeader")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .build();

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    // Add the OkHttpClient object to the Retrofit builder
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}



