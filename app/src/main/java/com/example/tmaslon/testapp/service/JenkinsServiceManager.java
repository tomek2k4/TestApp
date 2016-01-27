package com.example.tmaslon.testapp.service;


import android.content.Context;
import android.widget.Toast;

import com.example.tmaslon.testapp.model.Job;
import com.example.tmaslon.testapp.listadapter.JobsRecyclerViewAdapter;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.model.JobsListProvider;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
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

    private static final String JENKINS_API = "http://kra-tls.aaitg.com:8080";
    private final JenkinsService jenkinsRestService;
    private Context context;


    public JenkinsServiceManager(Context ctx) {
        context = ctx;

        OkHttpClient authorizationOkHttpClient = new OkHttpClient();
        authorizationOkHttpClient.interceptors().add(new AuthorizationInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JENKINS_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(authorizationOkHttpClient)
                .build();
        jenkinsRestService = retrofit.create(JenkinsService.class);
    }


    public void login(final String username, final String password,final Callback<JobsListProvider> callback){

        Call<JobsListProvider> call = jenkinsRestService.login();
        call.enqueue(new Callback<JobsListProvider>() {
            @Override
            public void onResponse(Response<JobsListProvider> response, Retrofit retrofit) {
                callback.onResponse(response,retrofit);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
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
