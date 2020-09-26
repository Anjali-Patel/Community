package com.gss.community.application;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class Community extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());

    }
}
