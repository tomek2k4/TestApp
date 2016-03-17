package com.example.tmaslon.testapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.tmaslon.testapp.JenkinsClientApplication;

/**
 * Created by tmaslon on 2016-02-11.
 */
public class RefreshService extends IntentService {

    private static final String TAG = "RefreshService";

    public RefreshService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent() called.");

        JenkinsServiceManager jsm = JenkinsClientApplication.getInstance().getJenkinsServiceManager();
        jsm.fetchAllJobs();
    }
}
