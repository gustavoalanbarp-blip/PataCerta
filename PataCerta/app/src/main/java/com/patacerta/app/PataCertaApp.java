package com.patacerta.app;

import android.app.Application;

import com.patacerta.app.notification.NotificationHelper;

public class PataCertaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.createChannel(this);
    }
}
