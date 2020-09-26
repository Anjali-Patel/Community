package com.gss.community.Activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gss.community.Adapter.MyAdapter;
import com.gss.community.Service.MyFirebaseMessagingService;
import com.gss.community.R;
import com.gss.community.Service.TrackMyNotificationsService;
import com.gss.community.Utility.SharedPreferenceUtils;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class DashboardActivity extends AppCompatActivity {
    RelativeLayout events, find_person, post_message, about_us, contact_us, logout_us, profile;
    ViewPager mPager;
    //   String count;
    ImageView notification;
    public static TextView notification_count;
    CircleIndicator indicator;
    SharedPreferenceUtils preferances;
    private static int currentPage = 0;
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    private static final Integer[] XMEN = {R.drawable.community1, R.drawable.community2, R.drawable.community3,R.drawable.community4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_dashboard);
        }catch(Exception e){
            e.printStackTrace();
        }
//        preferances = SharedPreferenceUtils.getInstance(DashboardActivity.this);
        events = findViewById(R.id.events);
        notification_count = findViewById(R.id.notification_count);
        logout_us = findViewById(R.id.logout_us);
        notification = findViewById(R.id.notification);
        find_person = findViewById(R.id.find_person);
        about_us = findViewById(R.id.about_us);
        post_message = findViewById(R.id.post_message);
        profile = findViewById(R.id.profile);
        contact_us = findViewById(R.id.contact_us);

        notification_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_count.setVisibility(View.GONE);
                clearNotification();
                Intent intent = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        try {
            if (MyFirebaseMessagingService.notification_count > 0) {
                notification_count.setVisibility(View.VISIBLE);
                notification_count.setText(MyFirebaseMessagingService.notification_count + "");
            } else {
                notification_count.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

        /*if (!isNotificationListenerActive(this)) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        } else {
            try {
                if (MyFirebaseMessagingService.notification_count > 0) {
                    notification_count.setVisibility(View.VISIBLE);
                    notification_count.setText(MyFirebaseMessagingService.notification_count + "");
                } else {
                    notification_count.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }*/
//        count= SharedPreferenceUtils.getInstance(DashboardActivity.this).getStringValue(CommanUtils.NOTIFICATIONCOUNT, "");
//        if (!count.equalsIgnoreCase("")){
////            notification.setBadg(Integer.parseInt(count));
//            ShortcutBadger.applyCount(DashboardActivity.this, Integer.parseInt(count));
//        }
//        else{
//
//        }

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearNotification();
                Intent intent = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(intent);

            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
                startActivity(intent);
            }
        });
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AboutUSActivity.class);
                startActivity(intent);
            }
        });
        logout_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.create();
                builder.setMessage("Are You Sure You Want to Logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferenceUtils.getInstance(DashboardActivity.this).clear();

                        Intent intent = new Intent(DashboardActivity.this, LoginPageActivity.class);

                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();


            }

        });

        find_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, FindPersonActivity.class);
                startActivity(intent);
            }
        });
        post_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PostMessageActivity.class);
                startActivity(intent);
            }
        });
        init();

    }

    private void clearNotification() {


        for(int i = 0; i< MyFirebaseMessagingService.myId.size(); i++){
            MyFirebaseMessagingService.notificationManager.cancel(MyFirebaseMessagingService.myId.get(i));
        }
        MyFirebaseMessagingService.myId.clear();
        MyFirebaseMessagingService.notification_count = 0;
    }

    public static boolean isNotificationListenerActive(Context context) {
        ComponentName cn = new ComponentName(context, TrackMyNotificationsService.class);
        String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());
        return enabled;
    }


        private void init() {
        for (int i = 0; i < XMEN.length; i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(DashboardActivity.this, XMENArray));

        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

    }

    @Override
    public void onBackPressed() {
      /*  if (back_pressed + 1000 > System.currentTimeMillis())
            finishAffinity();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();*/
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.create();
        builder.setMessage("Are You Sure You Want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
