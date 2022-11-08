package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Model.Response.ProductListResponse;

import java.util.List;

public interface ProductContract {
    interface Model{
        interface  OnFinishedListener{
            void  onFinished(ProductListResponse productListResponse);
            void  onFailure(Throwable t);
        }
        void getAllProduct(OnFinishedListener onFinishedListener);
        void getProductByPrice(OnFinishedListener onFinishedListener,float fPrice , float sPrice,String brandName,String order,String sortType);
    }
    interface View{
        void setProductList(List<Products> mListProduct);
        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void getProduct();
        void onGetProductByPrice(float fPrice , float sPrice,String brandName ,String order,String sortType);
    }
}
