package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.ProductDetail;
import com.example.duantotnghiep.Model.Response.AddressResponse;
import com.example.duantotnghiep.Model.Response.ProductDetailResponse;
import com.example.duantotnghiep.Model.UserAddress;

import java.util.List;

public interface DetailContract {
    interface DetailModel {
        interface OnGetDetailFinished {
            void onGetDetailSuccess(ProductDetailResponse productDetailResponse);

            void onGetDetailFailure(Throwable t);
        }

        void getProductDetail(OnGetDetailFinished onGetDetailFinished,int productId);
    }


    interface View {
        void getProductDetailSuccess(ProductDetail productDetails);

        void getProductDetailFailure(String msg);

        void getProductResponseFail(Throwable t);
    }

    interface Presenter {
        void onGetProductDetail(int detailId);
    }
}
