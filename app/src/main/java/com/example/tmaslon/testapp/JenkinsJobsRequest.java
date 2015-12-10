package com.example.tmaslon.testapp;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;

/**
 * Created by tmaslon on 2015-12-09.
 */
public class JenkinsJobsRequest extends AsyncTask<String,String,List<Job>> {

    private static final String JENKINS_API = "http://kra-tls.aaitg.com:8080/api";
    private Context context;
    private final JenkinsService jenkinsApiService;


    public interface JenkinsService {
        @GET("/json?tree=jobs[name,color,url]")
        Call<List<Job>> listJobs();
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
        return getAllJenkinsJobs();
    }


    public List<Job> getAllJenkinsJobs(){
        Call<List<Job>> call = jenkinsApiService.listJobs();
        List<Job> jobList = null;
        try {
            jobList = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
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
