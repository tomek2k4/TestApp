package com.example.tmaslon.testapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tmaslon on 2016-02-12.
 */
public class JobsDbOpenHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = 1;
    private static final String DBNAME = "jobs";

    public JobsDbOpenHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
