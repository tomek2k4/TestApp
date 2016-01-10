package com.example.tmaslon.testapp;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;





/**
 * Created by tmaslon on 2016-01-07.
 */
public class JobsRecyclerViewAdapter extends RecyclerView.Adapter<JobsRecyclerViewAdapter.JobsListViewHolder> {

    List<Job> jobList;

    public JobsRecyclerViewAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public JobsRecyclerViewAdapter.JobsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jenkins_job_list_item,parent,false);
        JobsListViewHolder jobsListViewHolder = new JobsListViewHolder(v);

        return jobsListViewHolder;
    }

    @Override
    public void onBindViewHolder(JobsRecyclerViewAdapter.JobsListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(jobList!=null){
            return jobList.size();
        }else{
            return 0;
        }
    }

    public static class JobsListViewHolder extends RecyclerView.ViewHolder{
        ImageView imgIcon;
        TextView txtTitle;

        public JobsListViewHolder(View itemView) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.last_build_status_image);
            txtTitle = (TextView) itemView.findViewById(R.id.job_name_text_view);
        }
    }


}
