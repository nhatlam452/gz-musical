package com.example.duantotnghiep.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duantotnghiep.Retrofit.RetrofitCallback;
import com.example.duantotnghiep.Retrofit.RetrofitController;
import com.example.duantotnghiep.Model.Photo;
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
        RetrofitController.ApiService.getService(getContext()).get_all_product().enqueue(RetrofitCallback.getAllProduct(getContext(),rcvProducts));
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