package com.example.duantotnghiep.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.duantotnghiep.Activities.MainActivity;
import com.example.duantotnghiep.Contract.NotificationContact;
import com.example.duantotnghiep.Model.MyNotification;
import com.example.duantotnghiep.Presenter.NotificationPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static MyFirebaseMessagingService instance;
    public static MyFirebaseMessagingService getInstance() {
        if (instance == null) {
            instance = new MyFirebaseMessagingService();
        }
        return instance;
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(LocalStorage.getInstance(getApplicationContext()).getLocalStorageManager().getUserInfo().getNotification() == 0){
            return;
        }
        // Check if the message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            // Get the notification data
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            long sentTime = remoteMessage.getSentTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date = simpleDateFormat.format(sentTime);
            Log.d("Notìication","Body : " + body + "sentTime : " + sentTime);
            // Create an intent to open the app when the notification is clicked
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_IMMUTABLE);

            //sendBroadcast
            Intent intentSend = new Intent("com.example.duantotnghiep.NOTIFICATION_RECEIVED");
            intentSend.putExtra("title", title);
            intentSend.putExtra("body", body);
            intentSend.putExtra("sentTime", date);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentSend);
            // Create the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                    .setSmallIcon(R.drawable.ic_logo_green)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Check if the device is running Android 8.0 or higher and create a notification channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel_id",
                        "Gz Musical",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            // Show the notification
            notificationManager.notify(0 /* ID of notification */, builder.build());
//            HomeFragment homeFragment = new HomeFragment();
//            homeFragment.onShowNotification();
        }
    }



}
