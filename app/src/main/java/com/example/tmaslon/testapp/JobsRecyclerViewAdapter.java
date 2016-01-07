package com.example.tmaslon.testapp;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;





/**
 * Created by tmaslon on 2016-01-07.
 */
public class JobsRecyclerViewAdapter extends RecyclerView.Adapter {

    List<Job> jobList;

    public JobsRecyclerViewAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(jobList!=null){
            return jobList.size();
        }else{
            return 0;
        }
    }



}
