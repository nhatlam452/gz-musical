package com.example.duantotnghiep.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.CartAdapter;
import com.example.duantotnghiep.Contract.CartContact;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Presenter.CartPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment implements CartContact.View {
    private final CartPresenter cartPresenter  = new CartPresenter(this);;
    private TextView tvCartPrice, tvCartTotalPrice;
    private List<Cart> mList;
    RecyclerView rcvCartFragment;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCartPrice = view.findViewById(R.id.tvCartPrice);
        rcvCartFragment = view.findViewById(R.id.rcvCartFragment);
        mList = new ArrayList<>();
        tvCartTotalPrice = view.findViewById(R.id.tvCartTotalPrice);
        cartPresenter.onGetCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationMain);
            view.findViewById(R.id.svCart).setOnTouchListener(new TranslateAnimation(getActivity(), bottomNavigationView));

        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                cartPresenter.onRemoveCart(mList.get(position).getCartId());
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvCartFragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onCartSuccess(List<Cart> cartList) {
        if (cartList == null) {
            cartPresenter.onGetCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));
            return;
        }
        mList = cartList;

        float sum = 0;
        for (int i = 0; i < cartList.size(); i++) {
            sum = sum + (cartList.get(i).getQuantity() * cartList.get(i).getPrice());
        }
        tvCartPrice.setText("$" + NumberFormat.getInstance().format(sum));
        tvCartTotalPrice.setText("$" + NumberFormat.getInstance().format(sum + 40000));

        CartAdapter cartAdapter = new CartAdapter(getContext(), cartList);
        rcvCartFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCartFragment.setAdapter(cartAdapter);

    }

    @Override
    public void onCartFailure(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartResponseFail(Throwable t) {
        Log.d("Cart Fragment", t.getMessage());
    }

    @Override
    public void onResume() {
        super.onResume();
        cartPresenter.onGetCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));
    }
}