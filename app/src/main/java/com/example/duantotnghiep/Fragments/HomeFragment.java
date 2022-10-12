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
import android.widget.ScrollView;

import com.example.duantotnghiep.Activities.MainActivity;
import com.example.duantotnghiep.Adapter.HomeNewsAdapter;
import com.example.duantotnghiep.Adapter.LoginPromotionAdapter;
import com.example.duantotnghiep.Model.Photo;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.SnapHelperOneByOne;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        MainActivity activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //initUI
        ImageView imgOpen = view.findViewById(R.id.imgHomeOpenSetting);
        rcvPromotionHome = view.findViewById(R.id.rcvPromotionHome);
        ScrollView svHome = view.findViewById(R.id.svHome);
        rcvNewsHome = view.findViewById(R.id.rcvNewsHome);
        circleIndicator2 = view.findViewById(R.id.ciPromotionHome);

        setRecycleViewPromotion();


        imgOpen.setOnClickListener(v -> {
            if (activity != null) {
                activity.OpenDrawer();
            }
        });
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationMain);
            svHome.setOnTouchListener(new TranslateAnimation(getActivity(), bottomNavigationView));

        }

        return view;
    }



    private void setRecycleViewPromotion() {
        List<Photo> mListPhoto = getListPhoto();
        LoginPromotionAdapter promotionAdapter = new LoginPromotionAdapter(mListPhoto);
        HomeNewsAdapter homeNewsAdapter = new HomeNewsAdapter(mListPhoto,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvPromotionHome.setLayoutManager(linearLayoutManager);
        rcvNewsHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(rcvPromotionHome);
        rcvPromotionHome.setAdapter(promotionAdapter);
        rcvNewsHome.setAdapter(homeNewsAdapter);
        circleIndicator2.attachToRecyclerView(rcvPromotionHome, linearSnapHelper);

    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.anh1, "2023 KAWAI MUSIC SCHOOL PIANO COMPETITION","https://vietthuong.vn/2023-kawai-music-school-piano-competition"));
        list.add(new Photo(R.drawable.anh2, "WORKSHOP PIANO: PIECES OF ART","https://vietthuong.vn/workshop-piano-pieces-of-art"));
        list.add(new Photo(R.drawable.anh3, "ĐỆM’S SECRET WORKSHOP","https://vietthuong.vn/dems-secret-workshop"));
        return list;
    }

}