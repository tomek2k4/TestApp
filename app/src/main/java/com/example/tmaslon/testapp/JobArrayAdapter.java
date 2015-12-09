package com.example.tmaslon.testapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tmaslon on 2015-12-09.
 */
public class JobArrayAdapter extends ArrayAdapter<Job>{


    Context context;
    int layoutResourceId;
    List<Job> jobList = null;

    public JobArrayAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutResourceId = resource;
        jobList = (List<Job>) objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        JobHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new JobHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.last_build_status_image);
            holder.txtTitle = (TextView)row.findViewById(R.id.job_name_text_view);

            row.setTag(holder);
        }
        else
        {
            holder = (JobHolder)row.getTag();
        }

        Job job = jobList.get(position);
        holder.txtTitle.setText(job.getName());

        ImageView lastBuildStatusImageView = (ImageView) row.findViewById(R.id.last_build_status_image);
        if(job.getColor().equals("red")) {
            lastBuildStatusImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.red));
        }else if(job.getColor().equals("blue")){
            lastBuildStatusImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.blue));
        }else{
            lastBuildStatusImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.yellow));
        }
        return row;
    }

    static class JobHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
