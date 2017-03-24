package com.example.tmaslon.testapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.tmaslon.testapp.JenkinsClientApplication;

/**
 * Created by tmaslon on 3/23/2017.
 */



/**
 * Service to handle sync requests.
 *
 * <p>This service is invoked in response to Intents with action android.content.SyncAdapter, and
 * returns a Binder connection to SyncAdapter.
 *
 * <p>For performance, only one sync adapter will be initialized within this application's context.
 *
 * <p>Note: The SyncService itself is not notified when a new sync occurs. It's role is to
 * manage the lifecycle of our {@link SyncAdapter} and provide a handle to said SyncAdapter to the
 * OS on request.
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
