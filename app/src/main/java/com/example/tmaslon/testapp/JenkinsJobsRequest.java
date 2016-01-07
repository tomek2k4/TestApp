package com.example.tmaslon.testapp;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.Toast;

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
public class JenkinsJobsRequest extends AsyncTask<String,String,JobsListResponse> {

    private static final String JENKINS_API = "http://192.168.0.12:8080";
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
    protected JobsListResponse doInBackground(String... strings) {
        Call<JobsListProvider> call = jenkinsApiService.retrieveJobsListProvider();
        JobsListProvider jlp = null;

        JobsListResponse jobsListResponse = new JobsListResponse();

        try {
            jlp = call.execute().body();
            List<Job> jobsList = jlp.getJobs();

            if(!jobsList.isEmpty()){
                jobsListResponse.setJobsStatusEnum(JobsStatusEnum.RESPONSE_OK);
                jobsListResponse.setJobsList(jobsList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            jobsListResponse.setJobsStatusEnum(JobsStatusEnum.CONN_FAILED);
        }catch (NullPointerException e){
            e.printStackTrace();
            jobsListResponse.setJobsStatusEnum(JobsStatusEnum.LIST_EMPTY);
        }

        return jobsListResponse;
    }

    @Override
    protected void onPostExecute(JobsListResponse jobsListResponse) {


        if(jobsListResponse!=null){

            switch (jobsListResponse.getJobsStatusEnum()){
                case LIST_EMPTY:
                    Toast.makeText(context,context.getText(R.string.jobs_list_empty_string),Toast.LENGTH_LONG).show();
                    break;
                case CONN_FAILED:
                    Toast.makeText(context,context.getText(R.string.connection_failed_string),Toast.LENGTH_LONG).show();
                    break;
                case RESPONSE_OK:
                    ArrayAdapter jenkinsArrayAdapter = ((MainActivity) context).getJenkinsJobsAdapter();
                    jenkinsArrayAdapter.addAll(jobsListResponse.getJobsList());
                    jenkinsArrayAdapter.notifyDataSetChanged();
                    Toast.makeText(context,context.getText(R.string.added_new_jobs_string),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
