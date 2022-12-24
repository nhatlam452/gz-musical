package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.MyNotification;
import com.example.duantotnghiep.Model.Response.NotificationResponse;

import java.util.List;

public interface NotificationContact {
        interface NotificationModel{
            interface  OnNotificationFinishedListener{
                void  onNotificationFinished(NotificationResponse notificationResponse);
                void  onNotificationFailure(Throwable t);
            }
            void sendNotification(OnNotificationFinishedListener notificationFinishedListener, String userId, String notification, String title, String timeSent);
            void getNotification(OnNotificationFinishedListener notificationFinishedListener, String userId);
            void setNotificationView(OnNotificationFinishedListener notificationView,String userId);
        }
        interface View{
            void onNotificationSuccess(List<MyNotification> notificationList);
            void onNotificationFailure(String msg);
            void onNotificationResponseFail(Throwable t);
        }

        interface Presenter {
            void onSendNotification(String userId, String notification, String title, String timeSent);
            void onGetNotification(String userId);
            void onSetNotificationView(String userId);
        }
    }

