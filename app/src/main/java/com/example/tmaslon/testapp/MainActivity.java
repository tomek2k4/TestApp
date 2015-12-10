package com.example.tmaslon.testapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    BaseAdapter jenkinsJobsAdapter;

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

        ListView jobsListView = (ListView)findViewById(R.id.jenkins_jobs_list_view);
        jenkinsJobsAdapter = new JobArrayAdapter(this,R.layout.jenkins_job_list_item, jenkinsInitialJobsList);

        jobsListView.setAdapter((ListAdapter) jenkinsJobsAdapter);
        jobsListView.setOnItemClickListener(this);

        JenkinsJobsRequest jobsRequest = new JenkinsJobsRequest(this);
        jobsRequest.execute();

    }

    public ArrayAdapter getJenkinsJobsAdapter() {
        return (ArrayAdapter)jenkinsJobsAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Job clickedItemJob = (Job) ((ArrayAdapter)jenkinsJobsAdapter).getItem(i);
        Toast.makeText(this,"Clicked on "+ clickedItemJob.getName(),Toast.LENGTH_SHORT).show();

        if(clickedItemJob.getUrl().length()>0){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(clickedItemJob.getUrl()));
            startActivity(intent);
        }
    }
}
