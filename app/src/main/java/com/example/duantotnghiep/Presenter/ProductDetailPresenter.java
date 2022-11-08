package com.example.duantotnghiep.Presenter;

import com.example.duantotnghiep.Contract.DetailContract;
import com.example.duantotnghiep.Model.Response.ProductDetailResponse;
import com.example.duantotnghiep.Service.ProductDetailService;

public class ProductDetailPresenter implements DetailContract.Presenter,DetailContract.DetailModel.OnGetDetailFinished {
    private final String TAG = "PRODUCT DETAIL PRESENTER";
    private final DetailContract.View detailView;
    private final DetailContract.DetailModel model;

    public ProductDetailPresenter(DetailContract.View detailView) {
        this.detailView = detailView;
        model = new ProductDetailService();
    }

    @Override
    public void onGetDetailSuccess(ProductDetailResponse productDetailResponse) {
        if (productDetailResponse.getResponseCode() == 1){
            detailView.getProductDetailSuccess(productDetailResponse.getData().get(0));
        }else {
            detailView.getProductDetailFailure(productDetailResponse.getMessage());
        }
    }

    @Override
    public void onGetDetailFailure(Throwable t) {
        detailView.getProductResponseFail(t);
        detailView.getProductDetailFailure(t.getMessage());

    }

    @Override
    public void onGetProductDetail(int detailId) {
        model.getProductDetail(this,detailId);
    }
}
