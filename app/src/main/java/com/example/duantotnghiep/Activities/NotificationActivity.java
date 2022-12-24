package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.duantotnghiep.Adapter.NotificationAdapter;
import com.example.duantotnghiep.Contract.NotificationContact;
import com.example.duantotnghiep.Model.MyNotification;
import com.example.duantotnghiep.Presenter.NotificationPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Service.MyFirebaseMessagingService;
import com.example.duantotnghiep.Utilities.LocalStorage;

import java.util.List;

public class NotificationActivity extends AppCompatActivity implements NotificationContact.View {
    RecyclerView rcvNotification;
    private NotificationPresenter notificationPresenter = new NotificationPresenter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        rcvNotification = findViewById(R.id.rcvNotification);
        List<MyNotification> notificationList = (List<MyNotification>) getIntent().getSerializableExtra("listNotification");
        NotificationAdapter notificationAdapter = new NotificationAdapter(notificationList);
        rcvNotification.setLayoutManager(new LinearLayoutManager(this));
        rcvNotification.setAdapter(notificationAdapter);
        notificationPresenter.onSetNotificationView(LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo().getUserId());
        findViewById(R.id.imgBackNoti).setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
    }

    @Override
    public void onNotificationSuccess(List<MyNotification> notificationList) {

    }

    @Override
    public void onNotificationFailure(String msg) {

    }

    @Override
    public void onNotificationResponseFail(Throwable t) {

    }
}