package com.example.duantotnghiep.Activities;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.LoginPromotionAdapter;
import com.example.duantotnghiep.BroadcastReceiver.NetworkBroadcastReceiver;
import com.example.duantotnghiep.Contract.NewsInterface;
import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.News;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.NewsPresenter;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppConstants;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.example.duantotnghiep.Utilities.SnapHelperOneByOne;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator2;

public class LoginActivity extends AppCompatActivity implements UserContract.View, NewsInterface {
    private RecyclerView rcvPromotion;
    private CallbackManager callbackManager;
    private CircleIndicator2 ciPromotion;
    private LoginPromotionAdapter promotionAdapter;
    private Button btnLogin;
    private CheckBox cbKeepLogged;
    private TextView tvSignUp, tvForgotPassword,tvGreet;
    private EditText edtPhoneNumberLogin, edtPasswordLogin;
    private UserPresenter userPresenter;
    private NewsPresenter newsPresenter;
    private SharedPreferences.Editor mEditor;
    private long backPressTime;
    private NetworkBroadcastReceiver networkChangeReceiver;

    private GoogleSignInClient mGoogleSignInClient;
    private final ActivityResultLauncher<Intent> ggLoginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {

                        Intent data = result.getData();
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_login);
        SharedPreferences mSharePrefer = getSharedPreferences(AppConstants.REMEMBER_LOGIN, 0);
        mEditor = mSharePrefer.edit();
        callbackManager = CallbackManager.Factory.create();
        networkChangeReceiver = new NetworkBroadcastReceiver(findViewById(R.id.tvConnectionLogin));
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
        boolean isLogin = mSharePrefer.getBoolean(AppConstants.isLogin, false);
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
            String phoneNumber = edtPhoneNumberLogin.getText().toString().trim();
            String password = edtPasswordLogin.getText().toString().trim();
            userPresenter.onLogin(phoneNumber, password);
        });
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, (jsonObject, graphResponse) -> {
                    try {
                        AppUtil.showDialog.show(LoginActivity.this);
                        String fbId = jsonObject.getString("id");
                        String fName = jsonObject.getString("first_name");
                        String lName = jsonObject.getString("last_name");

                        User user = new User(fbId, null, null, fName, lName, null);
                        cbKeepLogged.setChecked(true);
                        userPresenter.onSocialRegister(user, LoginActivity.this);
                    } catch (Exception e) {
                        Log.d("FBLogin", e.getMessage());
                        e.printStackTrace();
                    }
                });

                Log.d("LoginActivity", "thanh cong");
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,birthday,first_name,last_name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Huy", Toast.LENGTH_SHORT).show();
                Log.d("LoginActivity", "huy");


            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d("LoginActivity", e.getMessage());
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btnFbLogin).setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        });
        findViewById(R.id.btnGGLogin).setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            ggLoginLauncher.launch(signInIntent);
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            AppUtil.showDialog.show(LoginActivity.this);
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String fbId = account.getEmail();
            String fName = account.getDisplayName();

            User user = new User(null, fbId, null, fName, null, null);
            // Signed in successfully, show authenticated UI.
            cbKeepLogged.setChecked(true);

            userPresenter.onSocialRegister(user, LoginActivity.this);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LoginGGFailed", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setRecycleView(List<News> mListNews) {
        List<News> mList = new ArrayList<>();
        for (int i = 0; i < mListNews.size(); i++) {
            if (mListNews.get(i).getType() == 0) {
                mList.add(mListNews.get(i));
            }
        }
        promotionAdapter = new LoginPromotionAdapter(mList, this);
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
        tvGreet = findViewById(R.id.tvGreeting);
        edtPhoneNumberLogin = findViewById(R.id.edtPhoneNumberLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        ciPromotion = findViewById(R.id.ciPromotion);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        userPresenter = new UserPresenter(this);
        newsPresenter = new NewsPresenter(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int hour = LocalTime.now().getHour();
                    if (hour >= 5 && hour < 12) {
                        tvGreet.setText(getString(R.string.good_morning));
                    } else if (hour >= 12 && hour < 18) {
                        tvGreet.setText(getString(R.string.good_afternoon));
                    } else {
                        tvGreet.setText(getString(R.string.good_evening));
                    }
                }
            }
        }, 0, 1000);

    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                // Device is connected to a network
            } else {
                // Device is not connected to a network
            }
        }
    }

    @Override
    public void onSuccess(User user) {
        if (cbKeepLogged.isChecked()) {
            mEditor.putBoolean(AppConstants.isLogin, true);
            mEditor.apply();
        }
        LocalStorage.getInstance(this).getLocalStorageManager().setUserInfo(user);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        finish();
        AppUtil.showDialog.dismiss();

    }

    @Override
    public void onFail(String msg) {
        AppUtil.showDialog.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResponseFail(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("=== Error : ", t.getMessage());
        AppUtil.showDialog.dismiss();
    }

    @Override
    public void onNewsSuccess(List<News> listNews) {
        setRecycleView(listNews);
    }

    @Override
    public void onNewsFailure(Throwable t) {
        Log.d("LoginActivity", "Error : " + t);
    }

    @Override
    public void onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();
    }
}