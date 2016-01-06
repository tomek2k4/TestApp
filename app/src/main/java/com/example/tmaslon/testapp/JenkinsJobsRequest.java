package com.example.tmaslon.testapp;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;

/**
 * Created by tmaslon on 2015-12-09.
 */
public class JenkinsJobsRequest extends AsyncTask<String,String,List<Job>> {

    private static final String JENKINS_API = "http://192.168.0.14:8080";
    private Context context;
    private final JenkinsService jenkinsApiService;


    public interface JenkinsService {
        @GET("/api/json?tree=jobs[name,color,url]")
        Call<JobsListProvider> retrieveJobsListProvider();
    }

    public JenkinsJobsRequest(Context ctx) {
        context = ctx;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JENKINS_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jenkinsApiService = retrofit.create(JenkinsService.class);
    }


    @Override
    protected List<Job> doInBackground(String... strings) {
        Call<JobsListProvider> call = jenkinsApiService.retrieveJobsListProvider();
        JobsListProvider jlp = null;
        try {
            jlp = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getAllJenkinsJobsFromProvider(jlp);
    }


    public List<Job> getAllJenkinsJobsFromProvider(JobsListProvider jlp){

        List<Job> jobList = null;

        if(jlp != null){
            jobList = jlp.getJobs();
        }else{
            jobList = new LinkedList<Job>();
        }

        return jobList;
    }

    @Override
    protected void onPostExecute(List<Job> jobs) {
        if(jobs!=null){
            ArrayAdapter jenkinsArrayAdapter = ((MainActivity) context).getJenkinsJobsAdapter();
            jenkinsArrayAdapter.addAll(jobs);
            jenkinsArrayAdapter.notifyDataSetChanged();
        }
    }
}
