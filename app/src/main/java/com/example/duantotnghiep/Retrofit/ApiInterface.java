package com.example.duantotnghiep.Retrofit;

import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Model.Response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("views/check-login.php")
    Call<UserResponse> check_login(@Field("phoneNumber") String phoneNumber ,
                                   @Field("password") String password
    );
    @GET("views/get-all-product.php")
    Call<ProductListResponse> get_all_product();
}
