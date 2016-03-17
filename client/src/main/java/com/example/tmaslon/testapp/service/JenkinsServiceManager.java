package com.example.tmaslon.testapp.service;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.tmaslon.testapp.BuildConfig;
import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.data.JobsContract;
import com.example.tmaslon.testapp.exceptions.UndefinedColumnException;
import com.example.tmaslon.testapp.exceptions.UserNotAuthenticatedException;
import com.example.tmaslon.testapp.model.Job;
import com.example.tmaslon.testapp.model.JobsListProvider;
import com.example.tmaslon.testapp.model.User;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.AuthPermission;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;



/**
 * Created by tmaslon on 2015-12-09.
 */
public class JenkinsServiceManager {
    private static final String JENKINS_API = "http://10.239.69.57:8080/";
    private final JenkinsService jenkinsRestService;
    private Context context;
    private final Retrofit retrofit;


    public JenkinsServiceManager(Context ctx) {
        context = ctx;

        OkHttpClient authenticationOkHttpClient = new OkHttpClient();
        authenticationOkHttpClient.interceptors().add(new AuthenticationInterceptor());

        retrofit = new Retrofit.Builder()
                .baseUrl(JENKINS_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(authenticationOkHttpClient)
                .build();
        jenkinsRestService = retrofit.create(JenkinsService.class);
    }


    public void login(final String username, final String password,final Callback<JobsListProvider> callback){
        //Set user and password for authentication interceptor
        try{
//            if(BuildConfig.DEBUG){
//                ((AuthenticationInterceptor)retrofit.client().interceptors().get(0)).setUser(null);
//            }else{
                ((AuthenticationInterceptor)retrofit.client().interceptors().get(0)).setUser(new User(username, password));
//            }

        }catch (IndexOutOfBoundsException e){
            Log.e(JenkinsClientApplication.TAG, "Authentication interceptor was not defined: " + e.getMessage().toString());
        }

        Call<JobsListProvider> call = jenkinsRestService.login();
        call.enqueue(new Callback<JobsListProvider>() {
            @Override
            public void onResponse(Response<JobsListProvider> response, Retrofit retrofit) {
                try {
                    if (((AuthenticationInterceptor) retrofit.client().interceptors().get(0)).isAutenticated()) {
                        callback.onResponse(response, retrofit);
                    } else {
                        callback.onFailure(new UserNotAuthenticatedException(context.getResources().getString(R.string.user_not_authenticated_message)));
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.e(JenkinsClientApplication.TAG, "Authentication interceptor was not defined: " + e.getMessage().toString());
                    callback.onFailure(new UserNotAuthenticatedException(context.getResources().getString(R.string.user_not_authenticated_message)));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    //called form service, may take a while
    public void fetchAllJobs() {
        Log.d(JenkinsClientApplication.TAG,"fetchAllJobs() called");
        Call<JobsListProvider> call = jenkinsRestService.login();
        JobsListProvider jlp = null;
        List<Job> listFromJenkinsServer = null;
        try {
            jlp = call.execute().body();
            listFromJenkinsServer = jlp.getJobs();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(JenkinsClientApplication.TAG, "Failed to fetch data");

        }

        List<Job> jobsFromDB = new LinkedList<Job>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(JobsContract.CONTENT_URI, null, null, null, null);
        extractJobsFromCursor(jobsFromDB, cursor);

        Log.d(JenkinsClientApplication.TAG, "1: ");
        if(listFromJenkinsServer != null){
            for(Job jobFromServer: listFromJenkinsServer){
                Log.d(JenkinsClientApplication.TAG, "2: " + jobFromServer.getName() + jobFromServer.getColor());
                Boolean update = false;
                Boolean found = false;

                Job jobToInsert;
                for (Job jobFromDb : jobsFromDB) {
                    Log.d(JenkinsClientApplication.TAG,"db: " + jobFromDb.getName());
                    Log.d(JenkinsClientApplication.TAG,"srv: " + jobFromServer.getName());
                    if (jobFromDb.getName().equals(jobFromServer.getName())) {
                        found = true;
                        Log.d(JenkinsClientApplication.TAG,"c,db: " + jobFromDb.getColor());
                        Log.d(JenkinsClientApplication.TAG,"c,srv: " + jobFromServer.getColor());

                        if (!jobFromDb.getColor().equals(jobFromServer.getColor())) {
                            update = true;
                            jobToInsert = jobFromServer;
                            break;
                        }
                    }
                }
                if(update){
                    // update
                    Log.d(JenkinsClientApplication.TAG, "Call Content provider to make an update on the job");
                    ContentValues values = new ContentValues();
                    values.put(JobsContract.Columns.JOB_NAME,jobFromServer.getName());
                    values.put(JobsContract.Columns.COLOR,jobFromServer.getColor());
                    values.put(JobsContract.Columns.URL, jobFromServer.getUrl());
                    contentResolver.update(JobsContract.CONTENT_URI, values, jobFromServer.getName(), null);
                } else
                    if (found == false){
                        Log.d(JenkinsClientApplication.TAG,"Call Content provider to insert new job");
                        // insert
                        ContentValues values = new ContentValues();
                        values.put(JobsContract.Columns.JOB_NAME,jobFromServer.getName());
                        values.put(JobsContract.Columns.COLOR,jobFromServer.getColor());
                        values.put(JobsContract.Columns.URL,jobFromServer.getUrl());
                        Uri uri = contentResolver.insert(JobsContract.CONTENT_URI, values);
                    }
                    Log.d(JenkinsClientApplication.TAG,"no insert and no update");
                }
            }else{
                Log.d(JenkinsClientApplication.TAG,"list from server is null");
            }
    }

    public void executeBuild(final String jobName,final Callback<ResponseBody> callback){
        final Call<ResponseBody> call = jenkinsRestService.buildJob(jobName, "111");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                Log.d(JenkinsClientApplication.TAG,"Succeed in executing build");
                callback.onResponse(response, retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(JenkinsClientApplication.TAG,"Failed to execute build");
                callback.onFailure(t);
            }
        });

    }

    private void extractJobsFromCursor(List<Job> jobsFromDB, Cursor cursor) {
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                try {
                    String jobName = cursor.getString(JobsContract.Columns.getIndex(JobsContract.Columns.JOB_NAME));
                    String color = cursor.getString(JobsContract.Columns.getIndex(JobsContract.Columns.COLOR));
                    String url = cursor.getString(JobsContract.Columns.getIndex(JobsContract.Columns.URL));
                    Job job = new Job(jobName, color, url);
                    jobsFromDB.add(job);
                } catch (UndefinedColumnException ex) {
                    Log.d(JenkinsClientApplication.TAG, ex.getMessage());
                }
                cursor.moveToNext();
            }
        }

    }




}
