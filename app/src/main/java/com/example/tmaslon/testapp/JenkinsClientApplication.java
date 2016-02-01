package com.example.tmaslon.testapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.tmaslon.testapp.exceptions.UserNotDefinedException;
import com.example.tmaslon.testapp.manager.KeyManager;
import com.example.tmaslon.testapp.service.JenkinsServiceManager;

/**
 * Created by tmaslon on 2016-01-21.
 */
public class JenkinsClientApplication extends Application {
    private static JenkinsClientApplication instance;
    public static final String TAG = JenkinsClientApplication.class.getSimpleName();

    private KeyManager keyManager = null;
    private JenkinsServiceManager jenkinsServiceManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.d(TAG,TAG + " onCreate() called.");
    }

    public synchronized KeyManager getKeyManager() throws UserNotDefinedException{
        if(keyManager == null){
            throw new UserNotDefinedException(getResources().getString(R.string.user_not_defined_exception_messgae));
        }
        return keyManager;
    }

    public synchronized void setKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public synchronized void clearKeyManager(){
        if(keyManager != null ){
            keyManager.save("");
            keyManager = null;
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
