package com.example.duantotnghiep.Retrofit;


import com.example.duantotnghiep.Model.Comment;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Model.Response.AddressResponse;
import com.example.duantotnghiep.Model.Response.CartResponse;
import com.example.duantotnghiep.Model.Response.CommentResponse;
import com.example.duantotnghiep.Model.Response.LocationResponse;
import com.example.duantotnghiep.Model.Response.NewsResponse;
import com.example.duantotnghiep.Model.Response.OrderDetailResponse;
import com.example.duantotnghiep.Model.Response.OrderResponse;
import com.example.duantotnghiep.Model.Response.ProductDetailResponse;
import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Model.Response.StoreResponse;
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
    //Order Detail Api
    @FormUrlEncoded
    @POST("views/get-order-detail.php")
    Call<OrderDetailResponse> get_order_detail(@Field("orderId") int orderId);
    //Order Api
    @POST("views/insert-order.php")
    Call<OrderResponse> new_order(@Body Order order);
    @FormUrlEncoded
    @POST("views/get-order-by-user.php")
    Call<OrderResponse> get_all_order(@Field("userId") int userId);
    //Store Api
    @GET("views/get-all-store.php")
    Call<StoreResponse> get_all_store();
    //Comment Api
    @FormUrlEncoded
    @POST("views/get-all-comment.php")
    Call<CommentResponse> get_all_comment(@Field("productId") int productId);

    @POST("views/add-comment.php")
    Call<CommentResponse> add_comment(@Body Comment comment);

    //Cart Api
    @FormUrlEncoded
    @POST("views/get-cart-by.php")
    Call<CartResponse> get_all_cart(@Field("userId") int userId
    );
    @FormUrlEncoded
    @POST("views/remove-cart.php")
    Call<CartResponse> remove_cart(@Field("cartId") int cartId
    );
    @FormUrlEncoded
    @POST("views/add-to-cart.php")
    Call<CartResponse> add_to_cart(@Field("userId") int userId,
                                   @Field("productId") int productId,
                                   @Field("quantity") int quantity
    );

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

    @POST("views/social-login.php")
    Call<UserResponse> social_login(@Body User user);

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

    @FormUrlEncoded
    @POST("views/get-product-detail.php")
    Call<ProductDetailResponse> get_product_detail(@Field("productId") int productId);


    @FormUrlEncoded
    @POST("views/get-product-by-price.php")
    Call<ProductListResponse> get_product_by_price(@Field("fPrice") Float fPrice,
                                                   @Field("sPrice") Float sPrice,
                                                   @Field("brandName") String brand,
                                                   @Field("order") String order,
                                                   @Field("sortType") String type
    );

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
