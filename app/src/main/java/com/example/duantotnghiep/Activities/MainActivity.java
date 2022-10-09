package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.duantotnghiep.Adapter.ChangeFragmentAdapter;
import com.example.duantotnghiep.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout layoutSetting;
    private ViewPager2 vpMainActivity;
    private BottomNavigationView bottomNavigationMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vpMainActivity = findViewById(R.id.vpMainActivity);
        bottomNavigationMain = findViewById(R.id.bottomNavigationMain);
        layoutSetting = findViewById(R.id.layoutSetting);
        ImageView imgClose = findViewById(R.id.imgClose);
        setUpViewPager();

        imgClose.setOnClickListener(v -> {
            Transition transition = new Slide(Gravity.END);
            transition.setDuration(600);
            transition.addTarget(R.id.layoutSetting);

            TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);
            layoutSetting.setVisibility(View.GONE);
        });
    }
    public void OpenDrawer(){
        Transition transition = new Slide(Gravity.END);
        transition.setDuration(600);
        transition.addTarget(R.id.layoutSetting);

        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);
        layoutSetting.setVisibility(View.VISIBLE);
    }

    private void setUpViewPager() {
        ChangeFragmentAdapter changeFragmentAdapter = new ChangeFragmentAdapter(MainActivity.this);
        vpMainActivity.setAdapter(changeFragmentAdapter);
        vpMainActivity.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationMain.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationMain.getMenu().findItem(R.id.action_payment).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationMain.getMenu().findItem(R.id.action_buy).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationMain.getMenu().findItem(R.id.action_rewards).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationMain.getMenu().findItem(R.id.action_store).setChecked(true);
                        break;
                }

            }
        });
        vpMainActivity.setUserInputEnabled(false);


        bottomNavigationMain.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    vpMainActivity.setCurrentItem(0, false);
                    break;
                case R.id.action_payment:
                    vpMainActivity.setCurrentItem(1, false);
                    break;
                case R.id.action_buy:
                    vpMainActivity.setCurrentItem(2, false);
                    break;
                case R.id.action_rewards:
                    vpMainActivity.setCurrentItem(3, false);
                    break;
                case R.id.action_store:
                    vpMainActivity.setCurrentItem(4, false);
                    break;
            }
            return true;
        });
    }


}