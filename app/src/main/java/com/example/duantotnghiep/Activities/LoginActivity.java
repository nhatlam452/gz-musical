package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.LoginPromotionAdapter;
import com.example.duantotnghiep.Contract.NewsInterface;
import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.News;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.NewsPresenter;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.SnapHelperOneByOne;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator2;

public class LoginActivity extends AppCompatActivity implements UserContract.View, NewsInterface {
    private RecyclerView rcvPromotion;
    private CircleIndicator2 ciPromotion;
    private LoginPromotionAdapter promotionAdapter;
    private Button btnLogin;
    private CheckBox cbKeepLogged;
    private TextView tvSignUp, tvForgotPassword;
    private EditText edtPhoneNumberLogin, edtPasswordLogin;
    private UserPresenter userPresenter;
    private NewsPresenter newsPresenter;
    private SharedPreferences.Editor mEditor;
    private long backPressTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences mSharePrefer = getSharedPreferences(String.valueOf(R.string.REMEMBER_LOGIN), 0);
        mEditor = mSharePrefer.edit();
        boolean isLogin = mSharePrefer.getBoolean(String.valueOf(R.string.isLogin), false);
        if (isLogin) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
            finish();
        }
        initUi();
        newsPresenter.getAllNews();
        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        btnLogin.setOnClickListener(v -> {
            AppUtil.showDialog.show(this);
            if (cbKeepLogged.isChecked()) {
                mEditor.putBoolean(String.valueOf(R.string.isLogin), true);
                mEditor.apply();
            }
            String phoneNumber = edtPhoneNumberLogin.getText().toString().trim();
            String password = edtPasswordLogin.getText().toString().trim();
            userPresenter.onLogin(phoneNumber, password);
        });

    }




    private void setRecycleView(List<News> mListNews) {
        List<News> mList = new ArrayList<>();
        for (int i = 0;i < mListNews.size();i++){
            if (mListNews.get(i).getType() == 0){
                mList.add(mListNews.get(i));
            }
        }
        promotionAdapter = new LoginPromotionAdapter(mList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LoginActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rcvPromotion.setLayoutManager(linearLayoutManager);
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(rcvPromotion);

        rcvPromotion.setAdapter(promotionAdapter);
        ciPromotion.attachToRecyclerView(rcvPromotion, linearSnapHelper);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < (promotionAdapter.getItemCount() - 1)) {
                    linearLayoutManager.smoothScrollToPosition(rcvPromotion, new RecyclerView.State(), linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                } else {
                    linearLayoutManager.smoothScrollToPosition(rcvPromotion, new RecyclerView.State(), 0);

                }
            }
        }, 0, 3000);

    }

    private void initUi() {
        rcvPromotion = findViewById(R.id.vpPromotion);
        cbKeepLogged = findViewById(R.id.cbKeepLogged);
        edtPhoneNumberLogin = findViewById(R.id.edtPhoneNumberLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        ciPromotion = findViewById(R.id.ciPromotion);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        userPresenter = new UserPresenter(this);
        newsPresenter = new NewsPresenter(this);
    }




    @Override
    public void onSuccess(User user) {
        AppUtil.showDialog.dismiss();
        SharedPreferences mPrefs = getSharedPreferences("USER_INFO",MODE_PRIVATE);
        SharedPreferences.Editor mPrefEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mPrefEditor.putString("UserInfo",json);
        Log.d("====> ", "User : " + json);
        mPrefEditor.apply();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        finish();
    }

    @Override
    public void onFail(String msg) {
        AppUtil.showDialog.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseFail(Throwable t) {
        AppUtil.showDialog.dismiss();
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("=== Error : ", t.getMessage());
    }

    @Override
    public void onNewsSuccess(List<News> listNews) {
        setRecycleView(listNews);
    }

    @Override
    public void onNewsFailure(Throwable t) {
        Log.d("LoginActivity","Error : " + t);
    }
    @Override
    public void onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();
    }
}