package com.example.duantotnghiep.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.duantotnghiep.Fragments.HomeFragment;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Perform the action when the notification is received
        HomeFragment homeFragment = new HomeFragment();
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        String sentTime = intent.getStringExtra("sentTime");
        homeFragment.onSendNotification(body,title,sentTime);

    }
}

