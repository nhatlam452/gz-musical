package com.example.duantotnghiep.Service;

import android.util.Log;

import com.example.duantotnghiep.Contract.ProductContract;
import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Retrofit.ApiClient;
import com.example.duantotnghiep.Retrofit.ApiInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductService implements ProductContract.Model {
    private final String TAG_PRODUCT = "GET_ALL_PRODUCT";
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void getAllProduct(OnFinishedListener onFinishedListener) {
        Call<ProductListResponse> productCall = apiInterface.get_all_product();
        productCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful()){
                    ProductListResponse productListResponse = response.body();
                    onFinishedListener.onFinished(productListResponse);
                    Log.d(TAG_PRODUCT,"GET PRODUCT SUCCESS");
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Log.d(TAG_PRODUCT,t.getMessage());
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void getProductByPrice(OnFinishedListener onFinishedListener, float fPrice, float sPrice, String brandName,String order, String sortType) {
        Call<ProductListResponse> productCall = apiInterface.get_product_by_price(fPrice,sPrice,brandName,order,sortType);
        productCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful()){
                    ProductListResponse productListResponse = response.body();
                    onFinishedListener.onFinished(productListResponse);
                    Log.d("Product by price","GET PRODUCT SUCCESS");
                }
                Gson gson = new Gson();
                String json = gson.toJson(response.body().getData());
                Log.d("Product by price", "Response code : " + response.code() + "----" + response.body().getMessage());
                Log.d("Product by price", "Response : " +json);

            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Log.d("Product by price",t.getMessage());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
