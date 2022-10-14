package com.example.duantotnghiep.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.ProductsAdapter;
import com.example.duantotnghiep.Contract.ProductContract;
import com.example.duantotnghiep.Model.Photo;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Presenter.ProductPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class BuyFragment extends Fragment implements ProductContract.View {
    private RecyclerView rcvProducts;
    private List<Photo> mList;
    private ProductPresenter presenter;
    private String TAG = "BUY_FRAGMENT";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvProducts = view.findViewById(R.id.rcvProducts);
        presenter = new ProductPresenter(this);
        mList = getList();
        presenter.getProduct();
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationMain);
            rcvProducts.setOnTouchListener(new TranslateAnimation(getActivity(), bottomNavigationView));

        }
    }

    private List<Photo> getList() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img_10, "12th Dec 2021: Pistachio Christmas\n" +
                " Tree Frappuccino"));
        list.add(new Photo(R.drawable.img_16, "Art in a Cup: Buy One Get One"));
        list.add(new Photo(R.drawable.img_13, "Art in a Cup: Buy One Get One"));
        list.add(new Photo(R.drawable.img_17, "12th Dec 2021: Pistachio Christmas\n" +
                " Tree Frappuccino"));
        list.add(new Photo(R.drawable.img_10, "12th Dec 2021: Pistachio Christmas\n" +
                " Tree Frappuccino"));
        list.add(new Photo(R.drawable.img_16, "Art in a Cup: Buy One Get One"));
        list.add(new Photo(R.drawable.img_13, "Art in a Cup: Buy One Get One"));
        list.add(new Photo(R.drawable.img_17, "12th Dec 2021: Pistachio Christmas\n" +
                " Tree Frappuccino"));
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy, container, false);

    }

    @Override
    public void setProductList(List<Products> mListProduct) {
        ProductsAdapter adapter = new ProductsAdapter(getContext(), mListProduct);
        rcvProducts.setAdapter(adapter);
        rcvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvProducts.setHasFixedSize(true);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onResponseFail(Throwable t) {
        Log.d(TAG,t.getMessage());
    }
}