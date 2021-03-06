package com.example.tmaslon.testapp.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tmaslon.testapp.JenkinsClientApplication;

/**
 * Created by tmaslon on 2016-02-12.
 */
public class JobsDbOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = JobsDbOpenHelper.class.getSimpleName();


    public JobsDbOpenHelper(Context context) {
        super(context, JobsContract.DBNAME, null, JobsContract.DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Creating new database...");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ").append(JobsContract.TABLE_JOB).append(" (");
        sqlBuilder.append(JobsContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append(JobsContract.Columns.JOB_NAME + " TEXT NOT NULL, ");
        sqlBuilder.append(JobsContract.Columns.URL + " TEXT NOT NULL, ");
        sqlBuilder.append(JobsContract.Columns.COLOR + " TEXT NOT NULL ");
        sqlBuilder.append(");");

        try {
            sqLiteDatabase.execSQL(sqlBuilder.toString());
        } catch (SQLException ex) {
            Log.e(TAG, "Error creating application database.", ex);
        }
        Log.d(TAG, "... database creation finished.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG,"ProductDbOpenHelper onUpgrade called");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


}
