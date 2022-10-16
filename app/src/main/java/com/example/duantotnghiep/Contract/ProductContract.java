package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;

import java.util.List;

public interface ProductContract {
    interface Model{
        interface  OnFinishedListener{
            void  onFinished(ProductListResponse productListResponse);
            void  onFailure(Throwable t);
        }
        void getAllProduct(OnFinishedListener onFinishedListener);
    }
    interface View{
        void setProductList(List<Products> mListProduct);

        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void getProduct();
    }
}
