package com.example.tmaslon.testapp.service;

import com.example.tmaslon.testapp.model.JobsListProvider;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.Callback;


/**
 * Created by tmaslon on 2016-01-27.
 */
public interface JenkinsService {
    @GET("/api/json?tree=jobs[name,color,url]")
    Call<JobsListProvider>login();


    //void login(Callback<JobsListProvider> callback);
}
