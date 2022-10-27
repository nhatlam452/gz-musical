package com.example.duantotnghiep.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.MainActivity;
import com.example.duantotnghiep.Adapter.LoginPromotionAdapter;
import com.example.duantotnghiep.Contract.NewsInterface;
import com.example.duantotnghiep.Model.News;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.NewsPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.example.duantotnghiep.Utilities.SnapHelperOneByOne;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator2;


public class HomeFragment extends Fragment implements NewsInterface {
    private RecyclerView rcvPromotionHome, rcvNewsHome;
    private CircleIndicator2 circleIndicator2;
    private TextView tvGreetingHome;
    private ImageView imgOpen;
    private CircleImageView cimgAvt;
    private ImageView imgNotification;
    private RecyclerView rcvNotifications;
    private final int time = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //initUI
        initUI(view);


        MainActivity activity = (MainActivity) getActivity();
        imgOpen.setOnClickListener(v -> {
            if (activity != null) {
                activity.OpenDrawer();
            }
        });


        return view;
    }

    private void initUI(View view) {
        imgOpen = view.findViewById(R.id.imgHomeOpenSetting);
        cimgAvt = view.findViewById(R.id.cimgAvt);
        tvGreetingHome = view.findViewById(R.id.tvGreetingHome);
        TextView tvUserName = view.findViewById(R.id.tvUserName);
        rcvPromotionHome = view.findViewById(R.id.rcvPromotionHome);
        ScrollView svHome = view.findViewById(R.id.svHome);
        rcvNewsHome = view.findViewById(R.id.rcvNewsHome);
        circleIndicator2 = view.findViewById(R.id.ciPromotionHome);
        imgNotification = view.findViewById(R.id.imgNotificationHome);
        NewsPresenter newsPresenter = new NewsPresenter(this);

        User user = LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo();
        AppUtil.showDialog.show(getContext());
        newsPresenter.getAllNews();
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationMain);
            svHome.setOnTouchListener(new TranslateAnimation(getActivity(), bottomNavigationView));
        }
        if (user != null) {
            tvUserName.setText(user.getFirstName() + " " + user.getLastName());
            if (user.getAvt() != null) {
                cimgAvt.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(user.getAvt()).into(cimgAvt);
            }

        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int hour = LocalTime.now().getHour();
                    if (hour >= 5 && hour <= 12) {
                        tvGreetingHome.setText("Good morning");
                    } else if (hour >= 13 && hour <= 18) {
                        tvGreetingHome.setText("Good afternoon");
                    } else {
                        tvGreetingHome.setText("Good Evening");
                    }
                }
            }
        }, 0, 1000);


    }


    private void setRecycleViewPromotion(List<News> mListPromotion, List<News> mListNews) {
        LoginPromotionAdapter promotionAdapter = new LoginPromotionAdapter(mListPromotion, getContext());
        LoginPromotionAdapter newsAdapter = new LoginPromotionAdapter(mListNews, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvPromotionHome.setLayoutManager(linearLayoutManager);
        rcvNewsHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(rcvPromotionHome);
        rcvPromotionHome.setAdapter(promotionAdapter);
        rcvNewsHome.setAdapter(newsAdapter);
        circleIndicator2.attachToRecyclerView(rcvPromotionHome, linearSnapHelper);

    }


    @Override
    public void onNewsSuccess(List<News> mList) {
        List<News> mListPromotion = new ArrayList<>();
        List<News> mListNews = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getType() == 0) {
                mListPromotion.add(mList.get(i));
            } else {
                mListNews.add(mList.get(i));
            }
        }
        setRecycleViewPromotion(mListPromotion, mListNews);
        AppUtil.showDialog.dismiss();
    }

    @Override
    public void onNewsFailure(Throwable t) {
        Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
        Log.d("Home Fragment ===>", "Error : " + t);
        AppUtil.showDialog.dismiss();

    }
}