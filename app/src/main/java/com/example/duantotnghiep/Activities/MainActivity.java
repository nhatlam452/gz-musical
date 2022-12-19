package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager2.widget.ViewPager2;


import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Adapter.ChangeFragmentAdapter;
import com.example.duantotnghiep.Fragments.CartFragment;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout layoutSetting;
    private ViewPager2 vpMainActivity;
    private BottomNavigationView bottomNavigationMain;
    private long backPressTime;
    private ImageView imgClose;
    private ShareDialog shareDialog;
    private LinearLayout llUserInfo;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        SharedPreferences mSharePrefer = getSharedPreferences(String.valueOf(R.string.CHECK_PERMISSION), 0);
        mEditor = mSharePrefer.edit();
        initUi();
        checkMyPermission();
        imgClose.setOnClickListener(v -> closeDrawer());
        findViewById(R.id.tvAU).setOnClickListener(v -> {
            startWebView("https://vietthuong.vn/gioi-thieu.html");
            closeDrawer();
        });
        findViewById(R.id.tvHelp).setOnClickListener(v -> {
            startWebView("https://vietthuong.vn/huong-dan-mua-hang");
            closeDrawer();

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
            SharedPreferences sharedPreferences2 = getSharedPreferences(String.valueOf(R.string.REMEMBER_LOGIN), MODE_PRIVATE);
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

    private void checkMyPermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    mEditor.putBoolean(getString(R.string.iSLocationPermissionRequest), true);
                    mEditor.putBoolean(getString(R.string.isCameraPermissionRequest), true);
                    mEditor.putBoolean(getString(R.string.isWritePermissionRequest), true);
                } else {
                    for (int i = 0; i < multiplePermissionsReport.getGrantedPermissionResponses().size(); i++) {
                        switch (multiplePermissionsReport.getGrantedPermissionResponses().get(i).getPermissionName()) {
                            case "android.permission.CAMERA":
                                mEditor.putBoolean(getString(R.string.isCameraPermissionRequest), true);
                                break;
                            case "android.permission.ACCESS_FINE_LOCATION":
                                mEditor.putBoolean(getString(R.string.iSLocationPermissionRequest), true);
                                break;
                            case "android.permission.READ_EXTERNAL_STORAGE":
                                mEditor.putBoolean(getString(R.string.isWritePermissionRequest), true);
                                break;
                        }
                    }
                    mEditor.apply();
                }
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
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
                    tvEmail.setText("Phone number : " + AppUtil.formatPhoneNumber(user.getPhoneNumber()));

                }
            }
            Glide.with(this).load(user.getAvt()).into(cimgAvtSetting);
        }
        if (user.getPhoneNumber() == null) {
            llUserInfo.setVisibility(View.INVISIBLE);
        }
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


}