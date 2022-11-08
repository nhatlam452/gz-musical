package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Model.Response.CartResponse;
import com.example.duantotnghiep.Model.Response.ProductListResponse;

import java.util.List;

public interface CartContact {
    interface CartModel{
        interface  OnCartFinishedListener{
            void  onCartFinished(CartResponse cartResponse);
            void  onCartFailure(Throwable t);
        }
        void addToCart(OnCartFinishedListener onCartFinishedListener,int userId,int productId,int quantity);
        void getAllCart(OnCartFinishedListener onCartFinishedListener,int userId);
    }
    interface View{
        void onCartSuccess(List<Cart> cartList);
        void onCartFailure(String msg);
        void onCartResponseFail(Throwable t);
    }

    interface Presenter {
        void onGetCart(int userId);
        void onAddToCart(int userId,int productId,int quantity);
    }
}
