package com.example.tmaslon.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    BaseAdapter jenkinsJobsAdapter;
    List<Job> jenkinsJobsList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Job dummy = new Job("VeryImportentProject","red");
        Job dummy1 = new Job("AlphaCentauri","blue");
        Job dummy2 = new Job("Manhattan","green");
        jenkinsJobsList.add(dummy);
        jenkinsJobsList.add(dummy1);
        jenkinsJobsList.add(dummy2);

        ListView jobsListView = (ListView)findViewById(R.id.jenkins_jobs_list_view);
        jenkinsJobsAdapter = new JobArrayAdapter(this,R.layout.jenkins_job_list_item, jenkinsJobsList);

        jobsListView.setAdapter((ListAdapter) jenkinsJobsAdapter);

        JenkinsJobsRequest jobsRequest = new JenkinsJobsRequest(this);
        jobsRequest.execute();



    }

    public ArrayAdapter getJenkinsJobsAdapter() {
        return (ArrayAdapter)jenkinsJobsAdapter;
    }
}
