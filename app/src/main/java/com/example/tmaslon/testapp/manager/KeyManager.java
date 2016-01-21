package com.example.tmaslon.testapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tmaslon on 2016-01-21.
 */
public class KeyManager {
    private static final String KEY_PREFS = "key_preferences";
    private final SharedPreferences prefs;
    private static final String PREFS = "prefs";


    public KeyManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
