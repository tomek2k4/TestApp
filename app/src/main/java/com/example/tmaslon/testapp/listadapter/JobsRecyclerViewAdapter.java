package com.example.tmaslon.testapp.listadapter;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.model.Job;

import java.util.List;





/**
 * Created by tmaslon on 2016-01-07.
 */
public class JobsRecyclerViewAdapter extends RecyclerView.Adapter<JobsRecyclerViewAdapter.JobsListViewHolder> {

    List<Job> jobList;
    private Resources resources;

    public JobsRecyclerViewAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public JobsRecyclerViewAdapter.JobsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jenkins_job_list_item,parent,false);
        JobsListViewHolder jobsListViewHolder = new JobsListViewHolder(v);

        resources = parent.getContext().getResources();

        return jobsListViewHolder;
    }

    @Override
    public void onBindViewHolder(JobsRecyclerViewAdapter.JobsListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtTitle.setText(jobList.get(position).getName());

        Drawable drawable = resources.getDrawable(R.drawable.gray);
        switch (jobList.get(position).getColor()){
            case "red":
                drawable = resources.getDrawable(R.drawable.red);
                break;
            case "blue":
                drawable = resources.getDrawable(R.drawable.blue);
                break;
            case "yellow":
                drawable = resources.getDrawable(R.drawable.yellow);
                break;
        }
        holder.imgIcon.setImageDrawable(drawable);
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

    public List<Job> getJobList() {
        return jobList;
    }
}
