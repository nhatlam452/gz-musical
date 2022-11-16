package com.example.duantotnghiep.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duantotnghiep.Activities.ProductDetailActivity;
import com.example.duantotnghiep.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YoutubePlayerSupportFragmentX;


public class ProductDetailVideoFragment extends Fragment {
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView ypvHome;
    private ProductDetailActivity productDetailActivity ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productDetailActivity = (ProductDetailActivity) getActivity();
        String video = productDetailActivity.getDetail().getVideo();
        YoutubePlayerSupportFragmentX youTubePlayerSupportFragment = YoutubePlayerSupportFragmentX.newInstance();
        if (youTubePlayerSupportFragment == null)
            return;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.ypvProductDetail, youTubePlayerSupportFragment).commit();
        youTubePlayerSupportFragment.initialize("AIzaSyDPUEvuC6Anbi2Ywg12BM6vl41d8yIxtsw", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
                if (!b) {
                    youTubePlayer = player;

                    youTubePlayer.loadVideo(video);
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
}