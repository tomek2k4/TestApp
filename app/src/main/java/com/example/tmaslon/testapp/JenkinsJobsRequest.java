package com.example.tmaslon.testapp;


import android.content.Context;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;

/**
 * Created by tmaslon on 2015-12-09.
 */
public class JenkinsJobsRequest {

    private static final String JENKINS_API = "http://kra-tls.aaitg.com:8080/api";
    private Context context;
    private final JenkinsService jenkinsApiService;

    public interface JenkinsService {
        @GET("/json?tree=jobs[name,color,url]&pretty=true")
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

}
