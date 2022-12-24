package com.example.duantotnghiep.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.MainActivity;
import com.example.duantotnghiep.Activities.NotificationActivity;
import com.example.duantotnghiep.Adapter.LoginPromotionAdapter;
import com.example.duantotnghiep.BroadcastReceiver.NotificationReceiver;
import com.example.duantotnghiep.Contract.NewsInterface;
import com.example.duantotnghiep.Contract.NotificationContact;
import com.example.duantotnghiep.Model.MyNotification;
import com.example.duantotnghiep.Model.News;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.NewsPresenter;
import com.example.duantotnghiep.Presenter.NotificationPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.example.duantotnghiep.Utilities.SnapHelperOneByOne;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YoutubePlayerSupportFragmentX;


import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator2;


public class HomeFragment extends Fragment implements NewsInterface, NotificationContact.View {
    private RecyclerView rcvPromotionHome, rcvNewsHome;
    private CircleIndicator2 circleIndicator2;
    private LottieAnimationView lotteProgess;
    private TextView tvUnread;
    private ImageView imgOpen;
    private YouTubePlayer youTubePlayer;
    private List<MyNotification> myNotifications;
    private NewsPresenter newsPresenter;
    private final NotificationReceiver notificationReceiver = new NotificationReceiver();
    private final NotificationPresenter notificationPresenter = new NotificationPresenter(this);
    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                // Perform the desired action here
                lotteProgess.setVisibility(View.VISIBLE);
                newsPresenter.getAllNews();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //initUI
        initUI(view);
        initYoutubePlayer(view);
        onGetNotification();
        MainActivity activity = (MainActivity) getActivity();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(networkChangeReceiver, filter);


        imgOpen.setOnClickListener(v -> {
            if (activity != null) {
                activity.OpenDrawer();
            }
        });
        view.findViewById(R.id.imgNotificationHome).setOnClickListener(v -> {
            Intent i = new Intent(getContext(), NotificationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("listNotification", (Serializable) myNotifications);
            i.putExtras(bundle);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(notificationReceiver,
                new IntentFilter("com.example.duantotnghiep.NOTIFICATION_RECEIVED"));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(networkChangeReceiver);
    }

    private void initYoutubePlayer(View view) {
        YoutubePlayerSupportFragmentX youTubePlayerSupportFragment = YoutubePlayerSupportFragmentX.newInstance();
        if (youTubePlayerSupportFragment == null)
            return;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.vpYPVHome, youTubePlayerSupportFragment).commit();
        youTubePlayerSupportFragment.initialize("AIzaSyDPUEvuC6Anbi2Ywg12BM6vl41d8yIxtsw", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
                if (!b) {
                    youTubePlayer = player;
                    youTubePlayer.loadVideo("DchGOqXxN2w");
                    youTubePlayer.play();
                    youTubePlayer.setShowFullscreenButton(false);

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getContext(), "Please install Youtube App to load video", Toast.LENGTH_SHORT).show();
                Log.d("", youTubeInitializationResult.toString());
            }
        });

    }

    public void onSendNotification(String notification, String title, String sentTime) {
        Log.d("Notification", "okkkkkkkkkk");

        notificationPresenter.onSendNotification(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId(),
                notification, title, sentTime
        );
    }

    public void onGetNotification() {
        Log.d("Notification", "okkkkkkkkkk");
        notificationPresenter.onGetNotification(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId());
    }

    private void initUI(View view) {
        imgOpen = view.findViewById(R.id.imgHomeOpenSetting);
        YouTubePlayerView ypvHome = view.findViewById(R.id.ypvHome);
        CircleImageView cimgAvt = view.findViewById(R.id.cimgAvt);
        TextView tvUserName = view.findViewById(R.id.tvUserName);
        ScrollView svHome = view.findViewById(R.id.svHome);
        rcvNewsHome = view.findViewById(R.id.rcvNewsHome);
        lotteProgess = view.findViewById(R.id.lotteProgess);
        rcvPromotionHome = view.findViewById(R.id.rcvPromotionHome);
        tvUnread = view.findViewById(R.id.tvUnread);
        circleIndicator2 = view.findViewById(R.id.ciPromotionHome);
        lotteProgess.setVisibility(View.VISIBLE);
        newsPresenter = new NewsPresenter(this);

        User user = LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo();
        newsPresenter.getAllNews();
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationMain);
            svHome.setOnTouchListener(new TranslateAnimation(getActivity(), bottomNavigationView));
        }
        if (user != null) {
            if (user.getLastName() == null) {
                tvUserName.setText(user.getFirstName());

            } else {
                tvUserName.setText(user.getFirstName() + " " + user.getLastName());
            }
            if (user.getAvt() != null) {
                cimgAvt.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(user.getAvt()).into(cimgAvt);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onGetNotification();
        newsPresenter.getAllNews();
    }


    private void setRecycleViewPromotion(List<News> mListPromotion, List<News> mListNews) {
        LoginPromotionAdapter promotionAdapter = new LoginPromotionAdapter(mListPromotion, getContext());
        LoginPromotionAdapter newsAdapter = new LoginPromotionAdapter(mListNews, getContext());
        rcvNewsHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcvPromotionHome.setOnFlingListener(null);
        rcvPromotionHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
        lotteProgess.setVisibility(View.GONE);
    }

    @Override
    public void onNewsFailure(Throwable t) {
        Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
        Log.d("Home Fragment ===>", "Error : " + t);
        lotteProgess.setVisibility(View.GONE);


    }


    @Override
    public void onNotificationSuccess(List<MyNotification> notificationList) {
        if (notificationList == null) {
            notificationPresenter.onGetNotification(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId());
        } else {
            myNotifications = notificationList;
            int count = 0;
            for (MyNotification notification : notificationList) {
                if (notification.isViewed() == 0) {
                    count++;
                }
            }
            if (count == 0) {
                tvUnread.setVisibility(View.GONE);
            } else {
                tvUnread.setText(count + "");
            }
        }


    }

    @Override
    public void onNotificationFailure(String msg) {
    }

    @Override
    public void onNotificationResponseFail(Throwable t) {

    }

}