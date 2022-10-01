package com.example.duantotnghiep.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.duantotnghiep.Adapter.LoginPromotionAdapter;
import com.example.duantotnghiep.Models.Photo;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.SnapHelperOneByOne;

import java.util.ArrayList;
import java.util.List;


import me.relex.circleindicator.CircleIndicator2;


public class HomeFragment extends Fragment {
    private RecyclerView rcvPromotionHome, rcvNewsHome;
    private CircleIndicator2 circleIndicator2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView imgOpen = view.findViewById(R.id.imgDrawerOpen);
        rcvPromotionHome = view.findViewById(R.id.rcvPromotionHome);
        rcvNewsHome = view.findViewById(R.id.rcvNewsHome);
        circleIndicator2 = view.findViewById(R.id.ciPromotionHome);
        imgOpen.setOnClickListener(v -> {
            if (getActivity() != null) {
                AppUtil.sendBroadCastReceiver(getActivity(), true);
            }
        });

        setRecycleViewPromotion();

        return view;
    }

    private void setRecycleViewPromotion() {
        List<Photo> mListPhoto = getListPhoto();
        LoginPromotionAdapter promotionAdapter = new LoginPromotionAdapter(mListPhoto);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvPromotionHome.setLayoutManager(linearLayoutManager);
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(rcvPromotionHome);
        rcvPromotionHome.setAdapter(promotionAdapter);
        circleIndicator2.attachToRecyclerView(rcvPromotionHome, linearSnapHelper);

    }
    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img, "12th Dec 2021: Pistachio Christmas\n" +
                " Tree Frappuccino"));
        list.add(new Photo(R.drawable.img_4, "Art in a Cup: Buy One Get One"));
        list.add(new Photo(R.drawable.img_5, "12th Dec 2021: Pistachio Christmas\n" +
                " Tree Frappuccino"));
        return list;
    }

}