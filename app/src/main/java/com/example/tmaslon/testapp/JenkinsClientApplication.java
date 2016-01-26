package com.example.tmaslon.testapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.tmaslon.testapp.exceptions.UserNotDefinedException;
import com.example.tmaslon.testapp.manager.KeyManager;

/**
 * Created by tmaslon on 2016-01-21.
 */
public class JenkinsClientApplication extends Application {
    private static JenkinsClientApplication instance;

    public static final String TAG = JenkinsClientApplication.class.getSimpleName();
    KeyManager keyManager = null;

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

    public void setKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, TAG + " onTerminate() called.");
    }


    public static JenkinsClientApplication getInstance(){
        return instance;
    }

}
