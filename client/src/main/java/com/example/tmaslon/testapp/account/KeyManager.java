package com.example.tmaslon.testapp.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * Created by tmaslon on 2016-01-21.
 */
public class KeyManager {
    public static final String KEY_PREFS = "key_preferences";
    public static final String PREFS = "prefs";
    public static final String DEFAULT_KEY = "NOKEY";

    private final SharedPreferences sharedKeyPrefs;

    public KeyManager(Context context) {
        this.sharedKeyPrefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(String key){
        sharedKeyPrefs.edit().putString(KEY_PREFS, key).apply();
    }

    public String read() {
        if(sharedKeyPrefs.contains(KEY_PREFS)){
            return sharedKeyPrefs.getString(KEY_PREFS, DEFAULT_KEY);
        }else {
            return DEFAULT_KEY;
        }
    }


    public static String encodeCredentialsForBasicAuthorization(String username, String password){
        final String userAndPassword = username + ":" + password;
        return Base64.encodeToString(userAndPassword.getBytes(), Base64.NO_WRAP);
    }

}
