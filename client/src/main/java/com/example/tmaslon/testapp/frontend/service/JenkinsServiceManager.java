package com.example.tmaslon.testapp.frontend.service;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.tmaslon.testapp.BuildConfig;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.data.JobsContract;
import com.example.tmaslon.testapp.exceptions.UndefinedColumnException;
import com.example.tmaslon.testapp.exceptions.UserNotAuthenticatedException;
import com.example.tmaslon.testapp.frontend.model.Job;
import com.example.tmaslon.testapp.frontend.model.JobsListProvider;
import com.example.tmaslon.testapp.frontend.model.User;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;



/**
 * Created by tmaslon on 2015-12-09.
 */
public class JenkinsServiceManager {
    private static final String TAG = JenkinsServiceManager.class.getSimpleName();

    private static final String JENKINS_API = BuildConfig.JENKINS_API;
    private final JenkinsRemoteService jenkinsRestService;
    private Context context;
    private final Retrofit retrofit;
    private CookieManager cookieManager = new CookieManager();
    private final OkHttpClient authenticationOkHttpClient;


    public JenkinsServiceManager(Context ctx) {
        context = ctx;

        authenticationOkHttpClient = new OkHttpClient();
        authenticationOkHttpClient.interceptors().add(new AuthenticationInterceptor());
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        authenticationOkHttpClient.setCookieHandler(cookieManager);


        retrofit = new Retrofit.Builder()
                .baseUrl(JENKINS_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(authenticationOkHttpClient)
                .build();
        jenkinsRestService = retrofit.create(JenkinsRemoteService.class);
    }


    public void login(final String username, final String password,final Callback<ResponseBody> callback){
        //Set user and password for authentication interceptor
        try{
            ((AuthenticationInterceptor)retrofit.client().interceptors().get(0)).setUser(new User(username, password));
        }catch (IndexOutOfBoundsException e){
            Log.e(TAG, "Authentication interceptor was not defined: " + e.getMessage().toString());
        }

        Call<ResponseBody> call = jenkinsRestService.login();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                try {
                    if (((AuthenticationInterceptor) retrofit.client().interceptors().get(0)).isAuthenticated()) {
                        callback.onResponse(response, retrofit);
                    } else {
                        callback.onFailure(new UserNotAuthenticatedException(context.getResources().getString(R.string.user_not_authenticated_message)));
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, "Authentication interceptor was not defined: " + e.getMessage().toString());
                    callback.onFailure(new UserNotAuthenticatedException(context.getResources().getString(R.string.user_not_authenticated_message)));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public String getAuthToken(final String username, final String password,final String tokenType) throws IOException {

        String authToken = null;

        Request request = new Request.Builder()
                .url(BuildConfig.JENKINS_API + "j_acegi_security_check?"
                        + "j_username=" + username + "&j_password=" + password + "&from=%2F&Jenkins-Crumb=caf4002c35a4f18c92656f7838c88b6d&"
                        + "json=%7B%22j_username%22%3A+%22" + username + "%22%2C+%22j_password%22%3A+%22" + password + "%22%2C+%22"
                        + "remember_me%22%3A+false%2C+%22from%22%3A+%22%2F%22%2C+%22Jenkins-Crumb%22%3A+%22caf4002c35a4f18c92656f7838c88b6d%22%7D&"
                        + "Submit=log+in")
                .build();

        com.squareup.okhttp.Response response = authenticationOkHttpClient.newCall(request).execute();
        Log.d(TAG,response.code() + " " + response.toString() + " ");

        Log.d(TAG,"Cookies:");
        for(HttpCookie cookie : cookieManager.getCookieStore().getCookies()){
            Log.d(TAG,cookie.getName() + ":" + cookie.getValue());
            authToken = cookie.getValue();
        }
        return authToken;
    }

    //called form service, may take a while
    public void fetchAllJobs(SyncResult syncResult) throws IOException, UserNotAuthenticatedException {
        Log.d(TAG,"fetchAllJobs() called");
        Call<JobsListProvider> call = jenkinsRestService.listAllJobs();
        JobsListProvider jlp = null;
        List<Job> listFromJenkinsServer = null;

        jlp = call.execute().body();
        if(jlp == null){
            throw new UserNotAuthenticatedException();
        }

        listFromJenkinsServer = jlp.getJobs();

        ContentResolver contentResolver = context.getContentResolver();
        for(Job job:listFromJenkinsServer){
            syncResult.stats.numEntries++;
            ContentValues values = new ContentValues();
            values.put(JobsContract.Columns.JOB_NAME,job.getName());
            values.put(JobsContract.Columns.COLOR,job.getColor());
            values.put(JobsContract.Columns.URL,job.getUrl());
            Uri uri = contentResolver.insert(JobsContract.CONTENT_URI, values);
            if(uri!=null){
                //Log.d(JenkinsClientApplication.TAG,"Inserted new job: "+job.getName()+" to database");
                syncResult.stats.numInserts++;
            }else{
                int rowsAffected = contentResolver.update(JobsContract.CONTENT_URI,values,null,null);
                if(rowsAffected!=0){
                    //Log.d(JenkinsClientApplication.TAG,"Updated job with name: "+job.getName()+" in database");
                    syncResult.stats.numUpdates++;
                }
            }
        }
        Log.d(TAG,"Inserted " + syncResult.stats.numInserts +" jobs into database");
        Log.d(TAG,"Updated " + syncResult.stats.numUpdates +" jobs in database");
        Log.d(TAG,"Total number of jobs: " + syncResult.stats.numEntries);
    }

    public void executeBuild(final String jobName,final Callback<ResponseBody> callback){
        final Call<ResponseBody> call = jenkinsRestService.buildJob(jobName, "111");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                Log.d(TAG,"Succeed in executing build");
                callback.onResponse(response, retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG,"Failed to execute build");
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
                    Log.d(TAG, ex.getMessage());
                }
                cursor.moveToNext();
            }
        }

    }




}
