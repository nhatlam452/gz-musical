package com.example.duantotnghiep.Presenter;

import com.example.duantotnghiep.Contract.LoginContract;
import com.example.duantotnghiep.Contract.ProductContract;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Service.ProductService;

import java.util.List;

public class ProductPresenter implements ProductContract.Presenter,ProductContract.Model.OnFinishedListener {

    private ProductContract.View productView;
    private ProductContract.Model model;

    public ProductPresenter(ProductContract.View productView) {
        this.productView = productView;
        model = new ProductService();
    }


    @Override
    public void onFinished(ProductListResponse productListResponse) {
        productView.setProductList(productListResponse.getData());
        if (productListResponse.getData() != null){
            productView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        productView.onResponseFail(t);
        if (productView != null){
            productView.hideProgress();
        }
    }

    @Override
    public void getProduct() {
        if (productView == null){
            productView.showProgress();
        }
        model.getAllProduct(this);
    }
}
