package com.example.duantotnghiep.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.CartAdapter;
import com.example.duantotnghiep.Contract.CartContact;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Presenter.CartPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.LocalStorage;

import java.util.List;


public class CartFragment extends Fragment implements CartContact.View {
    private RecyclerView rcvCartFragment;
    private CartPresenter cartPresenter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvCartFragment = view.findViewById(R.id.rcvCartFragment);
        cartPresenter = new CartPresenter(this);
        cartPresenter.onGetCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onCartSuccess(List<Cart> cartList) {
        CartAdapter cartAdapter = new CartAdapter(getContext(),cartList);
        rcvCartFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCartFragment.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCartFailure(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartResponseFail(Throwable t) {
        Log.d("Cart Fragment",t.getMessage());
    }

    @Override
    public void onResume() {
        super.onResume();
        cartPresenter.onGetCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));

    }
}