package com.example.duantotnghiep.Presenter;

import android.util.Log;

import com.example.duantotnghiep.Contract.ProductContract;
import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Service.ProductService;

public class ProductPresenter implements ProductContract.Presenter,ProductContract.Model.OnFinishedListener {
    private final String TAG = "PRODUCT PRESENTER";
    private ProductContract.View productView;
    private ProductContract.Model model;

    public ProductPresenter(ProductContract.View productView) {
        this.productView = productView;
        model = new ProductService();
    }


    @Override
    public void onFinished(ProductListResponse productListResponse) {
        productView.setProductList(productListResponse.getData());

    }

    @Override
    public void onFailure(Throwable t) {
        productView.onResponseFail(t);
        if (productView != null){
            Log.d(TAG,t.getMessage());
        }
    }

    @Override
    public void getProduct() {
        model.getAllProduct(this);
    }
}
