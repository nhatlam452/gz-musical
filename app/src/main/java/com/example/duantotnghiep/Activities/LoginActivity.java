package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Model.Photo;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.SnapHelperOneByOne;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator2;

public class LoginActivity extends AppCompatActivity implements UserContract.View {
    private RecyclerView rcvPromotion;
    private CircleIndicator2 ciPromotion;
    private LoginPromotionAdapter promotionAdapter;
    private Button btnLogin;
    private CheckBox cbKeepLogged;
    private TextView tvSignUp, tvForgotPassword;
    private EditText edtPhoneNumberLogin, edtPasswordLogin;
    private UserPresenter userPresenter;
    private SharedPreferences.Editor mEditor;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences mSharePrefer = getSharedPreferences(String.valueOf(R.string.REMEMBER_LOGIN), 0);
        mEditor = mSharePrefer.edit();
        initUi();
        boolean isLogin = mSharePrefer.getBoolean(String.valueOf(R.string.REMEMBER_LOGIN), false);
        if (isLogin) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
            finish();
        }
        setRecycleView();
        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        btnLogin.setOnClickListener(v -> {
            String phoneNumber = edtPhoneNumberLogin.getText().toString().trim();
            String password = edtPasswordLogin.getText().toString().trim();
            userPresenter.onLogin(phoneNumber, password);
        });

    }

    private void setRecycleView() {
        List<Photo> mListPhoto = getListPhoto();
        promotionAdapter = new LoginPromotionAdapter(mListPhoto);
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
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img, null));
        list.add(new Photo(R.drawable.img_4, null));
        list.add(new Photo(R.drawable.img_5, null));
        return list;
    }


    @Override
    public void onSuccess(User user) {
        if (cbKeepLogged.isChecked()) {
            mEditor.putBoolean(String.valueOf(R.string.REMEMBER_LOGIN), true);
        }
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        finish();
    }

    @Override
    public void onFail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseFail(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("=== Error : ", t.getMessage());
    }
}