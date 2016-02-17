package com.example.tmaslon.testapp.listadapter;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.data.JobsContract;
import com.example.tmaslon.testapp.exceptions.UndefinedColumnException;
import com.example.tmaslon.testapp.model.Job;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tmaslon on 2016-01-07.
 */
public class JobsRecyclerViewAdapter extends RecyclerView.Adapter<JobsRecyclerViewAdapter.JobsListViewHolder> {

    private Context context;
    private CursorAdapter cursorAdapter;
    private JobsListViewHolder viewHolder;

    public JobsRecyclerViewAdapter(Context context) {
        cursorAdapter = new CursorAdapter(context,null,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // create a new view
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jenkins_job_list_item,parent,false);
                return v;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                try {
                    viewHolder.txtTitle.setText(cursor.getString(JobsContract.Columns.getIndex(JobsContract.Columns.JOB_NAME)));
                    Drawable drawable = context.getResources().getDrawable(R.drawable.gray);
                    switch (cursor.getString(JobsContract.Columns.getIndex(JobsContract.Columns.COLOR))){
                        case "red":
                            drawable = context.getResources().getDrawable(R.drawable.red);
                            break;
                        case "blue":
                            drawable = context.getResources().getDrawable(R.drawable.blue);
                            break;
                        case "yellow":
                            drawable = context.getResources().getDrawable(R.drawable.yellow);
                            break;
                    }
                    viewHolder.imgIcon.setImageDrawable(drawable);

                } catch (UndefinedColumnException e) {
                    e.printStackTrace();
                    Log.e(JenkinsClientApplication.TAG,e.getMessage());
                }
            }
        };

    }

    @Override
    public JobsRecyclerViewAdapter.JobsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
        return new JobsListViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(JobsRecyclerViewAdapter.JobsListViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        cursorAdapter.getCursor().moveToPosition(position);

        setViewHolder(holder);

        cursorAdapter.bindView(holder.itemView, context, cursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }


    /**
     * Swap the Cursor of the CursorAdapter and notify the RecyclerView.Adapter that data has
     * changed.
     * @param cursor The new Cursor representation of the data to be displayed.
     */
    public void swapCursor(Cursor cursor) {
        this.cursorAdapter.swapCursor(cursor);
        notifyDataSetChanged();
    }



    public static class JobsListViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.last_build_status_image)
        ImageView imgIcon;

        @InjectView(R.id.job_name_text_view)
        TextView txtTitle;

        public JobsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }


    private void setViewHolder(JobsListViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

}
