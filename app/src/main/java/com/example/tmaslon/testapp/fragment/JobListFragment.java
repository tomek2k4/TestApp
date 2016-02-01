package com.example.tmaslon.testapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tmaslon.testapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tmaslon on 2016-01-26.
 */
public class JobListFragment extends Fragment {

    @InjectView(R.id.jenkins_jobs_recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_list,container);
        ButterKnife.inject(this,view);
        return view;
    }
}
