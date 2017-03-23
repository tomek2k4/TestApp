package com.example.tmaslon.testapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.tmaslon.testapp.JenkinsClientApplication;

/**
 * Created by tmaslon on 3/23/2017.
 */

public class SyncService extends Service {
    private static final String TAG = "SyncService";

    private SyncAdapter syncAdapter = null;

    /**
     * Creates {@link SyncAdapter} instance.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Sync service created");
        syncAdapter = new SyncAdapter(getApplicationContext(),true);
    }

    /**
     * For logging only.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Sync service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
