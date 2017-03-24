package com.example.tmaslon.testapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.tmaslon.testapp.exceptions.UndefinedColumnException;

/**
 * Created by tmaslon on 2016-02-15.
 */
public class JobsContract {
    public static final int DBVERSION = 1;
    public static final String DBNAME = "jobs.db";

    public static final String CONTENT_AUTHORITY = "com.example.tmaslon.testapp.provider";
    public static final String BASE_PATH = "jobs";
    public static final Uri CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY +"/" + BASE_PATH);
    public static final String TABLE_JOB = "job";

    public static final class Columns implements BaseColumns {

        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.provider.jobs";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.provider.job";



        private Columns(){};
        public static final String JOB_NAME = "name";
        public static final String URL = "url";
        public static final String COLOR = "color";

        public static int getIndex(String columnName) throws UndefinedColumnException {
            int index;
            switch (columnName){
                case _ID:
                    index = 0;
                    break;
                case JOB_NAME:
                    index = 1;
                    break;
                case URL:
                    index = 2;
                    break;
                case COLOR:
                    index = 3;
                    break;
                default:
                    throw new UndefinedColumnException("Column "+ columnName+" is not defined in table "+ TABLE_JOB);
            }
            return index;
        }


    }






}
