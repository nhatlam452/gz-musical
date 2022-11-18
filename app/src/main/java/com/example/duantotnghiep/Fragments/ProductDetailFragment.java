package com.example.duantotnghiep.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Activities.ProductDetailActivity;
import com.example.duantotnghiep.Contract.CartContact;
import com.example.duantotnghiep.Contract.DetailContract;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.ProductDetail;
import com.example.duantotnghiep.Presenter.CartPresenter;
import com.example.duantotnghiep.Presenter.ProductDetailPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;

import java.text.NumberFormat;
import java.util.List;


public class ProductDetailFragment extends Fragment implements CartContact.View {

    int productId;
    private CartPresenter cartPresenter;
    private TextView tvTop, tvBack, tvNeck, tvFingerBoard, tvBridge, tvOrigin,tvQuantity,tvPriceDetail;
    private int q ;
    private float price;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_detail, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProductDetailActivity productDetailActivity = (ProductDetailActivity) getActivity();
        super.onViewCreated(view, savedInstanceState);
        q = productDetailActivity.quantity;
        price = (float) (productDetailActivity.getDetail().getPrice() * (100-productDetailActivity.getDetail().getDiscount())/100);
        Toast.makeText(productDetailActivity, ""+productDetailActivity.getDetail().getDiscount(), Toast.LENGTH_SHORT).show();
        cartPresenter = new CartPresenter(this);
        tvTop = view.findViewById(R.id.tvTop);
        tvBack = view.findViewById(R.id.tvBack);
        tvNeck = view.findViewById(R.id.tvNeck);
        tvFingerBoard = view.findViewById(R.id.tvFingerBoard);
        tvBridge = view.findViewById(R.id.tvBridge);
        tvOrigin = view.findViewById(R.id.tvOrigin);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        tvPriceDetail = view.findViewById(R.id.tvPriceProductDetail);
        tvPriceDetail.setText("$ " + NumberFormat.getInstance().format(price*q));
        tvQuantity.setText(q+"");
        ProductDetail productDetails = productDetailActivity.getDetail();
        tvTop.setText(productDetails.getTop());
        tvBack.setText(productDetails.getBack());
        tvNeck.setText(productDetails.getNeck());
        tvFingerBoard.setText(productDetails.getFingerBoard());
        tvBridge.setText(productDetails.getBridge());
        tvOrigin.setText(productDetails.getOrigin());
        view.findViewById(R.id.imgMinus).setOnClickListener(v->{
            if (q == 1)
                return;
            q --;
            tvQuantity.setText(q+"");
            tvPriceDetail.setText("$ " + NumberFormat.getInstance().format(price*q));


        });
        view.findViewById(R.id.imgPlus).setOnClickListener(v->{
            if (q == 5)
                return;
            q ++;
            tvQuantity.setText(q+"");
            tvPriceDetail.setText("$ " + NumberFormat.getInstance().format(price*q));


        });

        view.findViewById(R.id.btnAddToCart).setOnClickListener(v->{
            Log.d("Api put ==>","userID : " + Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId())
                    + "productId : " +  productDetailActivity.getProductId()
                    + "quantity : " + Integer.parseInt(tvQuantity.getText().toString())
            );
            cartPresenter.onAddToCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()),productDetailActivity.getProductId(),Integer.parseInt(tvQuantity.getText().toString()));
        });
    }




    @Override
    public void onCartSuccess(List<Cart> cartList) {
        Toast.makeText(getContext(),tvQuantity.getText().toString() + " products have been add to cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartFailure(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartResponseFail(Throwable t) {
        Toast.makeText(getContext(), "Unknown Error . Please check your connection", Toast.LENGTH_SHORT).show();
        Log.d("Add to Cart","Error : " + t.getMessage());
    }
}