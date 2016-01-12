package com.example.tmaslon.testapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mRecyclerViewAdapter;
    RecyclerView.LayoutManager mRecyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Job> jenkinsInitialJobsList = new LinkedList<>();
        Job dummy = new Job("VeryImportentProject","red");
        Job dummy1 = new Job("AlphaCentauri","blue");
        Job dummy2 = new Job("Manhattan","green");
        jenkinsInitialJobsList.add(dummy);
        jenkinsInitialJobsList.add(dummy1);
        jenkinsInitialJobsList.add(dummy2);

        initializeRecyclerView(jenkinsInitialJobsList);

        JenkinsJobsRequest jobsRequest = new JenkinsJobsRequest(this);
        jobsRequest.execute();

    }

    public JobsRecyclerViewAdapter getJenkinsJobsRecyclerViewAdapter() {
        return (JobsRecyclerViewAdapter) mRecyclerViewAdapter;
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Job clickedItemJob = (Job) ((ArrayAdapter)jenkinsJobsAdapter).getItem(i);
//        Toast.makeText(this,"Clicked on "+ clickedItemJob.getName(),Toast.LENGTH_SHORT).show();
//
//        if(clickedItemJob.getUrl().length()>0){
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(clickedItemJob.getUrl()));
//            startActivity(intent);
//        }
//    }

    public void initializeRecyclerView(List<Job> jenkinsInitialJobsList){

        mRecyclerView = (RecyclerView) findViewById(R.id.jenkins_jobs_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewAdapter = new JobsRecyclerViewAdapter(jenkinsInitialJobsList);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

    }



}
