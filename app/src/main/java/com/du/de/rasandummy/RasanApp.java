package com.du.de.rasandummy;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class RasanApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
