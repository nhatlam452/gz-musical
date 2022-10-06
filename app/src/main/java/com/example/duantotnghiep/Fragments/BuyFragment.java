package com.example.duantotnghiep.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.LinearLayout;

import com.example.duantotnghiep.Adapter.ProductsAdapter;
import com.example.duantotnghiep.Models.Photo;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class BuyFragment extends Fragment {
    private RecyclerView rcvProducts;
    private List<Photo> mList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvProducts = view.findViewById(R.id.rcvProducts);
        mList = getList();
        ProductsAdapter productsAdapter = new ProductsAdapter(getContext(),mList);
        rcvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvProducts.setHasFixedSize(true);
        rcvProducts.setAdapter(productsAdapter);

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
}