package com.pr.productkereview.utils;

import android.app.Application;
import android.content.Intent;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.pr.productkereview.activities.WelcomeActivity;


public class MyApp extends Application {

    private static final String ONESIGNAL_APP_ID = "ed0db632-e10f-4d79-9b48-df7c033aaa7a";
    public static MyApp mInstance;

    public MyApp() {
        mInstance = this;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(new ExampleNotificationOpenedHandler());
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }


    private class ExampleNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {
            Intent intent = new Intent(MyApp.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}

// jks file password:  12345