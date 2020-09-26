package com.gss.community.Service;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.gss.community.Activities.DashboardActivity;

public class TrackMyNotificationsService extends NotificationListenerService {

    public static int notification_count = 0;
    public TrackMyNotificationsService() {}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        IBinder mIBinder = super.onBind(intent);
        return mIBinder;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if (sbn != null) {
            Bundle extras = sbn.getNotification().extras;

            String key = sbn.getKey();
            String pkgName = sbn.getPackageName();
            if (pkgName.equals("com.gss.community")) {
                //code
                notification_count++;
                DashboardActivity.notification_count.setText(notification_count +"");
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}