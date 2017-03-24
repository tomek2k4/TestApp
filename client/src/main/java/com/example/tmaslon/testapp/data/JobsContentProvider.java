package com.example.tmaslon.testapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.exceptions.UndefinedColumnException;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by tmaslon on 2016-02-15.
 */
public class JobsContentProvider extends ContentProvider{

    // used for the UriMacher
    private static final int JOBS = 10;
    private static final int JOB_NAME = 20;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(JobsContract.CONTENT_AUTHORITY, JobsContract.BASE_PATH, JOBS);
        sURIMatcher.addURI(JobsContract.CONTENT_AUTHORITY, JobsContract.BASE_PATH + "/*", JOB_NAME);
    }

    //database
    private JobsDbOpenHelper dbHelper = null;

    @Override
    public boolean onCreate() {
        dbHelper = new JobsDbOpenHelper(getContext());
        return (dbHelper != null) ? true : false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(JobsContract.TABLE_JOB);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case JOBS:
                break;
            case JOB_NAME:
                // adding the job name to the original query
                queryBuilder.appendWhere(JobsContract.Columns.JOB_NAME + "="
                        +"\""+ uri.getLastPathSegment()+"\"");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id = findJobIdByName(contentValues);
        if(id!=-1){
            //Found id of job with this name in database check if need to update
            return null;
        }

        id = db.insert(JobsContract.TABLE_JOB, null, contentValues);
        if (id != 0) {
            Log.d(JenkinsClientApplication.TAG,"Inserting new row succed");
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        } else {
            Log.d(JenkinsClientApplication.TAG,"Failed to insert new row");
            return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        long id = findJobIdByName(contentValues);
        if(id!=-1){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //contentValues.put(JobsContract.Columns._ID,id);
            db.update(JobsContract.TABLE_JOB,contentValues,"_id = ?",new String[]{Long.toString(id)});
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }

        return 0;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case JOBS:
                return JobsContract.Columns.CONTENT_TYPE;
            case JOB_NAME:
                return JobsContract.Columns.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }


    private long findJobIdByName(ContentValues contentValues){
        long jobId = -1;
        String jobName = contentValues.getAsString(JobsContract.Columns.JOB_NAME);
        Uri uriJobByName = Uri.parse(JobsContract.CONTENT_URI.toString()+"/"+jobName);
        Cursor cursor = query(uriJobByName, null, null, null, null);
        try {
            if(cursor.getCount()!=0) {
                if (cursor.moveToFirst()) {
                    jobId = cursor.getLong(JobsContract.Columns.getIndex(JobsContract.Columns._ID));
                }
            }
        } catch (UndefinedColumnException e) {
            Log.d(JenkinsClientApplication.TAG,e.getMessage());
        } finally {
            cursor.close();
        }
        return jobId;
    }



    private void checkColumns(String[] projection) {
        String[] available = {JobsContract.Columns._ID, JobsContract.Columns.JOB_NAME,
                JobsContract.Columns.URL, JobsContract.Columns.COLOR};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}
