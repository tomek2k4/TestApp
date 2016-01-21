package com.example.tmaslon.testapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by tmaslon on 2016-01-21.
 */
public class JenkinsClientApplication extends Application {

    private static final String TAG = JenkinsClientApplication.class.getSimpleName();
    private SharedPreferences prefs;


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
