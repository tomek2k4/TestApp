package com.example.tmaslon.testapp;

import android.app.Application;
import android.util.Log;

import com.example.tmaslon.testapp.manager.KeyManager;
import com.example.tmaslon.testapp.service.JenkinsServiceManager;
import com.google.common.base.Optional;

/**
 * Created by tmaslon on 2016-01-21.
 */
public class JenkinsClientApplication extends Application {
    private static JenkinsClientApplication instance;
    public static final String TAG = JenkinsClientApplication.class.getSimpleName();

    private Optional<KeyManager> keyManager = Optional.absent();
    private JenkinsServiceManager jenkinsServiceManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.d(TAG,TAG + " onCreate() called.");
    }

    public synchronized Optional<KeyManager> getKeyManager(){
        return this.keyManager;
    }

    public synchronized void setKeyManager(KeyManager keyManager) {
        this.keyManager = Optional.fromNullable(keyManager);
    }

    public synchronized void clearKeyManager(){
        if(keyManager.isPresent()){
            keyManager.get().save("");
            keyManager = Optional.absent();
        }
    }


    public synchronized JenkinsServiceManager getJenkinsServiceManager() {
        if(jenkinsServiceManager == null){
            jenkinsServiceManager = new JenkinsServiceManager(this);
        }
        return jenkinsServiceManager;
    }

    public static JenkinsClientApplication getInstance(){
        return instance;
    }

    @Override
    public void onTerminate() {
        //We will delete stored key on application terminate
        clearKeyManager();
        super.onTerminate();
        Log.d(TAG, TAG + " onTerminate() called.");
    }
}
