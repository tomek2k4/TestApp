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
        jenkinsJobsList.add(dummy);

        ListView jobsListView = (ListView)findViewById(R.id.jenkins_jobs_list_view);
        jenkinsJobsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, jenkinsJobsList);
        
        jobsListView.setAdapter((ListAdapter)jenkinsJobsAdapter);

    }




}
