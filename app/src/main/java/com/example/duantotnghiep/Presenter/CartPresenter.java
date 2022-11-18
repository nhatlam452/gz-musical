package com.example.duantotnghiep.Presenter;

import android.icu.number.CompactNotation;
import android.util.Log;

import com.example.duantotnghiep.Contract.CartContact;
import com.example.duantotnghiep.Model.Response.CartResponse;
import com.example.duantotnghiep.Service.CartService;
import com.google.gson.Gson;

public class CartPresenter implements CartContact.Presenter, CartContact.CartModel.OnCartFinishedListener {
    private CartContact.View mCartView;
    private CartContact.CartModel mCartModel;

    public CartPresenter(CartContact.View mCartView) {
        this.mCartView = mCartView;
        mCartModel = new CartService();
    }

    @Override
    public void onCartFinished(CartResponse cartResponse) {
        if (cartResponse.getResponseCode() ==1 ){
            mCartView.onCartSuccess(cartResponse.getData());
        }else {
            mCartView.onCartFailure(cartResponse.getMessage());
        }
        Gson gson = new Gson();
        String json = gson.toJson(cartResponse.getData());
        Log.d("Cart Presenter","Response code : " + cartResponse.getResponseCode() +
                "Msg : " + cartResponse.getMessage() +
                "Data : " + json
                );
    }

    @Override
    public void onCartFailure(Throwable t) {
        mCartView.onCartResponseFail(t);
    }

    @Override
    public void onGetCart(int userId) {
        mCartModel.getAllCart(this,userId);
    }

    @Override
    public void onAddToCart(int userId, int productId, int quantity) {
        mCartModel.addToCart(this,userId,productId,quantity);
    }

    @Override
    public void onRemoveCart(int cartId) {
        mCartModel.removeCart(this,cartId);
    }
}
