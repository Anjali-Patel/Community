package com.gss.community.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.webkit.WebView;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gss.community.Activities.DashboardActivity;
import com.gss.community.Activities.MessageNotificationDetailActivity;
import com.gss.community.Activities.NotificationDetailActivity;
import com.gss.community.R;
import com.gss.community.Utility.CommanUtils;
import com.gss.community.Utility.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
int n=0;
 public static  ArrayList<Integer> myId = new ArrayList<Integer>();

    SharedPreferenceUtils preferances;
    public static int notification_count = 0;
    WebView video;
    String files;
    public static NotificationManager  notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        int notificationId = new Random().nextInt(60000);
        myId.add(notificationId);
        Intent intent = null;
        if(remoteMessage.getData().get("type").equals("events")){
            intent = new Intent(MyFirebaseMessagingService.this, NotificationDetailActivity.class);
        }else {
            intent = new Intent(MyFirebaseMessagingService.this, MessageNotificationDetailActivity.class);
        }
        intent.putExtra("notification_id", notificationId);
        intent.putExtra("notification_title", remoteMessage.getData().get("title"));
        intent.putExtra("notification_body", remoteMessage.getData().get("body"));
        intent.putExtra("notification_time", remoteMessage.getData().get("time_f"));
        intent.putExtra("notification_date", remoteMessage.getData().get("date_f"));
        intent.putExtra("notification_file", remoteMessage.getData().get("files"));
        if(remoteMessage.getData().get("type").equals("events")){
            intent.putExtra("notification_type", "Event");
        }else {
            intent.putExtra("notification_type", "Message");
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingintent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
           notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(remoteMessage.getData().get("body"));
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "123")
                .setSmallIcon(R.drawable.community)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
//                .setSubText("Message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setContentIntent(pendingintent);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

        notification_count++;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        preferances=SharedPreferenceUtils.getInstance(this);
        String refreshedToken = s;
        preferances.setValue(CommanUtils.FCM, refreshedToken);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(String body){
        //CharSequence adminChannelName = "test";
        String adminChannelDescription = "123";
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel("123", body, NotificationManager.IMPORTANCE_DEFAULT);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}

