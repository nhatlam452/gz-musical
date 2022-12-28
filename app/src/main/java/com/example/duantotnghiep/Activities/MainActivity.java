package com.example.duantotnghiep.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager2.widget.ViewPager2;


import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Adapter.ChangeFragmentAdapter;
import com.example.duantotnghiep.Adapter.SpinnerAdapter;
import com.example.duantotnghiep.BroadcastReceiver.NetworkBroadcastReceiver;
import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Fragments.CartFragment;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppConstants;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements UserContract.View {
    private RelativeLayout layoutSetting;
    private ViewPager2 vpMainActivity;
    private BottomNavigationView bottomNavigationMain;
    private Switch switchNotification;
    private long backPressTime;
    private ImageView imgClose;
    private ShareDialog shareDialog;
    private LinearLayout llUserInfo;
    private final UserPresenter userPresenter = new UserPresenter(this);
    private SharedPreferences.Editor mEditor;
    private NetworkBroadcastReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        SharedPreferences mSharePrefer = getSharedPreferences(AppConstants.CHECK_PERMISSION, 0);
        mEditor = mSharePrefer.edit();
        initUi();
        TextView layout = findViewById(R.id.tvConnectionMain);
        networkChangeReceiver = new NetworkBroadcastReceiver(layout);

//        onGetNotification();
        imgClose.setOnClickListener(v -> closeDrawer());
        findViewById(R.id.tvAU).setOnClickListener(v -> {
            startWebView("https://vietthuong.vn/gioi-thieu.html");
            closeDrawer();
        });
        findViewById(R.id.tvHelp).setOnClickListener(v -> {
            startWebView("https://vietthuong.vn/huong-dan-mua-hang");
            closeDrawer();

        });
        findViewById(R.id.tvLanguage).setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            mList.add("Tiếng Việt");
            mList.add("English");
            openDialogLanguage(mList, "Ngôn ngữ");

        });
        findViewById(R.id.tvPrivacyPolice).setOnClickListener(v -> {
            startWebView("https://vietthuong.vn/chinh-sach-thanh-toan-va-bao-mat");
            closeDrawer();

        });
        findViewById(R.id.tvTOS).setOnClickListener(v -> {
            startWebView("https://vietthuong.vn/dieu-khoan-su-dung-website");
            closeDrawer();
        });
        findViewById(R.id.tvContactUsSetting).setOnClickListener(v -> openDialogContact());
        findViewById(R.id.tvLogOut).setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
            SharedPreferences.Editor mEditor = sharedPreferences.edit();
            mEditor.clear();
            mEditor.apply();
            SharedPreferences sharedPreferences2 = getSharedPreferences((AppConstants.REMEMBER_LOGIN), MODE_PRIVATE);
            SharedPreferences.Editor mEditor2 = sharedPreferences2.edit();
            mEditor2.clear();
            mEditor2.apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        findViewById(R.id.imgChangeUserInfo).setOnClickListener(v ->
                {
                    startActivity(new Intent(this, UserInfoActivity.class));
                    overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
                    closeDrawer();

                }

        );
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppUtil.showDialog.show(this);

            if (isChecked) {
                switchNotification.setChecked(true);
                userPresenter.onUpdateNotification(1, LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo().getUserId());
            } else {
                switchNotification.setChecked(false);
                userPresenter.onUpdateNotification(0, LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo().getUserId());
            }

        });
        findViewById(R.id.tvSavedAddress).setOnClickListener(v -> {
            startActivity(new Intent(this, SavedAddressActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
            closeDrawer();

        });
        findViewById(R.id.tvTellYourFiend).setOnClickListener(v -> {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setQuote("Gz Musical")
                    .setContentUrl(Uri.parse("https://youtube.com"))
                    .build();
            shareDialog.show(linkContent);
        });
        findViewById(R.id.imgYoutube).setOnClickListener(v -> goToUrl("https://www.youtube.com/channel/UCR_v4LC7mFpxZow1uWxognw"));
        findViewById(R.id.imgFb).setOnClickListener(v -> goToUrl("https://www.facebook.com/profile.php?id=100008612558105"));
        findViewById(R.id.imgInsta).setOnClickListener(v -> goToUrl("https://www.instagram.com/nhatlam_isme/"));

    }

    private void openDialogLanguage(List<String> mList, String title) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_spinner);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        TextView tvTitle = dialog.findViewById(R.id.tvSpinnerTitle);
        RecyclerView rcvSpinner = dialog.findViewById(R.id.rcvSpinner);
        SharedPreferences mPreferences = getSharedPreferences(AppConstants.LANGUAGE, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        tvTitle.setText(title);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, mList, s -> {
            dialog.dismiss();

            if (s.equals("Tiếng Việt")) {
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale = new Locale("vi"); // Đặt ngôn ngữ mới là tiếng Việt
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                editor.putString("language", "vi");
            } else if (s.equals("English")) {
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale = new Locale("en"); // Đặt ngôn ngữ mới là tiếng Anh
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                editor.putString("language", "en");
            }
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        rcvSpinner.setLayoutManager(new LinearLayoutManager(this));
        rcvSpinner.setAdapter(spinnerAdapter);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }





    private void goToUrl(String url) {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void closeDrawer() {
        Transition transition = new Slide(Gravity.END);
        transition.setDuration(600);
        transition.addTarget(R.id.layoutSetting);

        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);
        layoutSetting.setVisibility(View.GONE);
    }

    private void initUi() {
        shareDialog = new ShareDialog(this);
        vpMainActivity = findViewById(R.id.vpMainActivity);
        switchNotification = findViewById(R.id.switchNotification);
        llUserInfo = findViewById(R.id.llUserInfo);
        CircleImageView cimgAvtSetting = findViewById(R.id.cimgAvtSetting);
        bottomNavigationMain = findViewById(R.id.bottomNavigationMain);
        TextView tvUserName = findViewById(R.id.tvUserNameSetting);
        TextView tvEmail = findViewById(R.id.tvEmailSetting);
        layoutSetting = findViewById(R.id.layoutSetting);
        imgClose = findViewById(R.id.imgClose);
        setUpViewPager();
        User user = LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo();
        if (user != null) {
            tvUserName.setText(user.getFirstName() + " " + user.getLastName());
            if (user.getEmail() != null) {
                tvEmail.setText(user.getEmail());
            } else {
                if (user.getPhoneNumber() == null) {
                } else {
                    tvEmail.setText(getResources().getString(R.string.phone_number) + " : " + AppUtil.formatPhoneNumber(user.getPhoneNumber()));

                }
            }
            Glide.with(this).load(user.getAvt()).into(cimgAvtSetting);
        }
        if (user.getPhoneNumber() == null) {
            llUserInfo.setVisibility(View.INVISIBLE);
        }
        switchNotification.setChecked(user.getNotification() == 1);
    }

    private void openDialogContact() {
        Dialog contactDialog = new Dialog(this);
        contactDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        contactDialog.setContentView(R.layout.contact_dialog);
        Window window = contactDialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        window.findViewById(R.id.tvHotline).setOnClickListener(v1 -> {
            String phone = "0909916020";
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
            closeDrawer();
        });
        window.findViewById(R.id.tvEmailUS).setOnClickListener(v1 -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"nhatlam452@gmail.com"});
            try {
                startActivity(Intent.createChooser(i, "Send mail ....."));
            } catch (Exception e) {
                Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
            }
            closeDrawer();
        });

        contactDialog.show();
    }

    private void startWebView(String url) {
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra("URL", url);
        startActivity(i);
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }

    public void OpenDrawer() {
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

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onSuccess(User user) {
        LocalStorage.getInstance(this).getLocalStorageManager().setUserInfo(user);
        AppUtil.showDialog.dismiss();

    }

    @Override
    public void onFail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        AppUtil.showDialog.dismiss();

    }

    @Override
    public void onResponseFail(Throwable t) {
        AppUtil.showDialog.dismiss();
        Log.d("Update Notification", t.getMessage());
        Toast.makeText(this, "Client Error. Please check your connection", Toast.LENGTH_SHORT).show();

    }

}