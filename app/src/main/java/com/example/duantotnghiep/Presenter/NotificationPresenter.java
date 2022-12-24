package com.example.duantotnghiep.Presenter;

import com.example.duantotnghiep.Contract.NotificationContact;
import com.example.duantotnghiep.Model.Response.NotificationResponse;
import com.example.duantotnghiep.Service.NotificationService;

public class NotificationPresenter implements NotificationContact.Presenter, NotificationContact.NotificationModel.OnNotificationFinishedListener {
    private final NotificationContact.View view;
    private final NotificationContact.NotificationModel model;

    public NotificationPresenter(NotificationContact.View view) {
        this.view = view;
        model = new NotificationService();
    }

    @Override
    public void onNotificationFinished(NotificationResponse notificationResponse) {
        if (notificationResponse.getResponseCode() == 1) {
            view.onNotificationSuccess(notificationResponse.getData());
        } else {
            view.onNotificationFailure(notificationResponse.getMessage());
        }
    }

    @Override
    public void onNotificationFailure(Throwable t) {
        view.onNotificationResponseFail(t);
    }


    @Override
    public void onSendNotification(String userId, String notification, String title, String timeSent) {
        model.sendNotification(this, userId, notification, title, timeSent);
    }

    @Override
    public void onGetNotification(String userId) {
        model.getNotification(this, userId);
    }

    @Override
    public void onSetNotificationView(String userId) {
        model.setNotificationView(this,userId);
    }
}

