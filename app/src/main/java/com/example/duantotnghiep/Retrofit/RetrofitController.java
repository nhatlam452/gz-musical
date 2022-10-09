package com.example.duantotnghiep.Retrofit;

import android.content.Context;

import com.example.duantotnghiep.Model.Response.ProductResponse;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class RetrofitController {
    private static class RetrofitBuilder {

        private static final String BASE_URL = "http://192.168.137.1/API_DATN/";
        private final Retrofit retrofit = buildRetrofit();
        private static RetrofitBuilder instance;
        private final Context context;

        private RetrofitBuilder(Context context) {
            this.context = context;
        }


        public static RetrofitBuilder getInstance(Context context) {
            if (instance == null) {
                instance = new RetrofitBuilder(context);
            }
            return instance;
        }

        private static Retrofit buildRetrofit() {
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
//                      /*
//                       chain.request() returns original request that you can work with(modify, rewrite)
//                       */
//
//                    // Here you can rewrite the request
//
//                    /*
//                     chain.proceed(request) is the call which will initiate the HTTP work. This call invokes the request and returns the response as per the request.
//                    */
//                    Response response = chain.proceed(request);
//
//                    //Here you can rewrite/modify the response
//
//                    return response;
//                    String token = AppHelper.getInstance(instance.context);
//                    Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
                    Request request = chain.request();
                    return chain.proceed(request);
                }
            };
            OkHttpClient okHttpClient = (
                    new OkHttpClient.Builder())
                    .addInterceptor(interceptor)// dùng để thêm Interceptor của App
                   // .addNetworkInterceptor(new CustomInterceptor()) Dùng để thêm Interceptor của Network
                    .build();
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient) // The Htttp client dùng để gửi request
                    .build();
            return retrofit;
        }
        public  <T> T createService(Class<T> service){
            return retrofit.create(service);
        }

    }
    public static class ApiService{

        private static IApiService service;
        public static IApiService getService(Context context){
            return service = RetrofitBuilder.getInstance(context).createService(IApiService.class);
        }

    }
    public interface IApiService{
        @FormUrlEncoded
        @POST("views/check-login.php")
        Call<UserResponse> check_login(@Field("phoneNumber") String phoneNumber ,
                                       @Field("password") String password
                                       );
        @GET("views/get-all-product.php")
        Call<ProductResponse> get_all_product();



    }
}
