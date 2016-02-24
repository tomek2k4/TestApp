package com.example.tmaslon.testapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * Created by tmaslon on 2016-01-21.
 */
public class KeyManager {
    private static final String KEY_PREFS = "key_preferences";
    private final SharedPreferences sharedKeyPrefs;
    private static final String PREFS = "prefs";

    public KeyManager(Context context) {
        this.sharedKeyPrefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(String key){
        sharedKeyPrefs.edit().putString(KEY_PREFS, key).apply();
    }

    public String read() {
        return sharedKeyPrefs.getString(KEY_PREFS, "");
    }


    public static String encodeCredentialsForBasicAuthorization(String username, String password){
        final String userAndPassword = username + ":" + password;
        return Base64.encodeToString(userAndPassword.getBytes(), Base64.NO_WRAP);
    }

}