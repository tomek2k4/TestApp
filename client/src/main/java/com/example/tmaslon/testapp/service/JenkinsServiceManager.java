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

        if(listFromJenkinsServer != null){
            for(Job jobFromServer: listFromJenkinsServer){
                Boolean update = false;
                Boolean found = false;

                Job jobToInsert;
                for (Job jobFromDb : jobsFromDB) {
                    if (jobFromDb.getName().equals(jobFromServer.getName())) {
                        found = true;
                        if (!jobFromDb.getColor().equals(jobFromServer.getColor())) {
                            update = true;
                            jobToInsert = jobFromServer;
                            break;
                        }
                    }
                }
                if(update){
                    // update
                    Log.d(JenkinsClientApplication.TAG,"Call Content provider to make an update on the job");
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

    private void extractJobsFromCursor(List<Job> jobsFromDB, Cursor cursor) {
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                try {
                    String jobName = cursor.getString(JobsContract.Columns.getIndex(JobsContract.Columns.JOB_NAME));
                    String color = cursor.getString(JobsContract.Columns.getIndex(JobsContract.Columns.COLOR));
                    String url = cursor.getString(JobsContract.Columns.getIndex(JobsContract.Columns.URL));
                    Job job = new Job(jobName, color, url);
                } catch (UndefinedColumnException ex) {
                    Log.d(JenkinsClientApplication.TAG, ex.getMessage());
                }
                cursor.moveToNext();
            }
        }

    }
//    @Override
//    protected JobsListResponse doInBackground(String... strings) {
//        Call<JobsListProvider> call = jenkinsRestService.retrieveJobsListProvider();
//        JobsListProvider jlp = null;
//
//        JobsListResponse jobsListResponse = new JobsListResponse();
//
//        try {
//            jlp = call.execute().body();
//            List<Job> jobsList = jlp.getJobs();
//
//            if(!jobsList.isEmpty()){
//                jobsListResponse.setJobsStatusEnum(JobsStatusEnum.RESPONSE_OK);
//                jobsListResponse.setJobsList(jobsList);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            jobsListResponse.setJobsStatusEnum(JobsStatusEnum.CONN_FAILED);
//        }catch (NullPointerException e){
//            e.printStackTrace();
//            jobsListResponse.setJobsStatusEnum(JobsStatusEnum.LIST_EMPTY);
//        }
//
//        return jobsListResponse;
//    }
//
//    @Override
//    protected void onPostExecute(JobsListResponse jobsListResponse) {
//        if(jobsListResponse!=null){
//
//            switch (jobsListResponse.getJobsStatusEnum()){
//                case LIST_EMPTY:
//                    Toast.makeText(context,context.getText(R.string.jobs_list_empty_string),Toast.LENGTH_LONG).show();
//                    break;
//                case CONN_FAILED:
//                    Toast.makeText(context,context.getText(R.string.connection_failed_string),Toast.LENGTH_LONG).show();
//                    break;
//                case RESPONSE_OK:
//                    JobsRecyclerViewAdapter jobsRecyclerViewAdapter = null;// = ((MainActivity) context).getJenkinsJobsRecyclerViewAdapter();
//                    jobsRecyclerViewAdapter.getJobList().addAll(jobsListResponse.getJobsList());
//                    jobsRecyclerViewAdapter.notifyItemChanged(jobsRecyclerViewAdapter.getJobList().size()-1);
//                    Toast.makeText(context,context.getText(R.string.added_new_jobs_string),Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    }
}
