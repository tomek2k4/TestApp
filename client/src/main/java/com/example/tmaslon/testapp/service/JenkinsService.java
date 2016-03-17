package com.example.tmaslon.testapp.service;

import com.example.tmaslon.testapp.model.JobsListProvider;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Created by tmaslon on 2016-01-27.
 */
public interface JenkinsService {
    @GET("/api/json?tree=jobs[name,color,url]")
    Call<JobsListProvider>login();

    @POST("/job/{job_name}/build")
    Call<ResponseBody> buildJob(@Path("job_name") String jobName, @Query("token") String token);

}
