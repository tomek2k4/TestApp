package com.example.tmaslon.testapp.frontend.service;

import com.example.tmaslon.testapp.frontend.model.JobsListProvider;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Created by tmaslon on 2016-01-27.
 */
public interface JenkinsRemoteService {

    @GET("/api/json")
    Call<ResponseBody>login();

    @GET("/api/json?tree=jobs[name,color,url]")
    Call<JobsListProvider>listAllJobs();

    @POST("/job/{job_name}/build")
    Call<ResponseBody> buildJob(@Path("job_name") String jobName, @Query("token") String token);

}
