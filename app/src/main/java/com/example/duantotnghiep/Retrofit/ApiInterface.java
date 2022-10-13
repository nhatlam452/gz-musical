package com.example.duantotnghiep.Retrofit;

import com.example.duantotnghiep.Model.Location;
import com.example.duantotnghiep.Model.Response.LocationResponse;
import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Model.Response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("views/check-login.php")
    Call<UserResponse> check_login(@Field("phoneNumber") String phoneNumber ,
                                   @Field("password") String password
    );
    @GET("views/get-all-product.php")
    Call<ProductListResponse> get_all_product();
    @GET("province")
    Call<LocationResponse> getCity();
    @GET("district")
    Call<LocationResponse> getDistrict(@Query("province") String province);
    @GET("commune")
    Call<LocationResponse> getWard(@Query("district")
                                    String district);
}
