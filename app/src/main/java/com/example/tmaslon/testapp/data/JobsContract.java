package com.example.tmaslon.testapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tmaslon on 2016-02-15.
 */
public class JobsContract {
    public static final int DBVERSION = 1;
    public static final String DBNAME = "jobs";

    public static final String AUTHORITY = "com.example.tmaslon.testapp.data.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/status");
    public static final String TABLE_JOB = "job";

    public static final class Columns implements BaseColumns{
        private Columns(){};
        public static final String JOB_NAME = "name";
        public static final String URL = "url";
        public static final String COLOR = "color";
    }

}
