package com.example.duantotnghiep.Retrofit;


import com.example.duantotnghiep.Model.Response.AddressResponse;
import com.example.duantotnghiep.Model.Response.LocationResponse;
import com.example.duantotnghiep.Model.Response.NewsResponse;
import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Model.UserAddress;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    //User Api
    @FormUrlEncoded
    @POST("views/check-login.php")
    Call<UserResponse> check_login(@Field("phoneNumber") String phoneNumber,
                                   @Field("password") String password
    );
    @FormUrlEncoded
    @POST("views/update-user.php")
    Call<UserResponse> update_info(@Field("param") String param,
                                   @Field("value") String value,
                                   @Field("userId") String userId
    );
    @FormUrlEncoded
    @POST("views/check-user-exits.php")
    Call<UserResponse> check_user_exits(@Field("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST("views/update-user-password.php")
    Call<UserResponse> change_user_password(@Field("phoneNumber") String phoneNumber,
                                            @Field("newPassword") String newPassword,
                                            @Field("password") String password

    );
    @POST("views/insert-user.php")
    Call<UserResponse> register_user(@Body User user);
    //Address Api
    @FormUrlEncoded
    @POST("views/get-all-address.php")
    Call<AddressResponse> get_user_address(@Field("userId") String userId
    );
    @POST("views/insert-address.php")
    Call<AddressResponse> insert_address(@Body UserAddress address);
    //Product Api
    @GET("views/get-all-product.php")
    Call<ProductListResponse> get_all_product();
    //News Api
    @GET("views/get-all-news.php")
    Call<NewsResponse> get_all_news();
    //Location Api
    @GET("province")
    Call<LocationResponse> getCity();

    @GET("district")
    Call<LocationResponse> getDistrict(@Query("province") String province);

    @GET("commune")
    Call<LocationResponse> getWard(@Query("district")
                                           String district);


}
