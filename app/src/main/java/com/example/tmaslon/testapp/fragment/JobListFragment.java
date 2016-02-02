package com.example.tmaslon.testapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tmaslon.testapp.MainActivity;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.listadapter.JobsRecyclerViewAdapter;
import com.example.tmaslon.testapp.model.Job;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tmaslon on 2016-01-26.
 */
public class JobListFragment extends Fragment {

    @InjectView(R.id.jenkins_jobs_recycler_view)
    RecyclerView recyclerView;

    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private MainActivity mainActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_list,container,false);
        ButterKnife.inject(this,view);


        List<Job> jenkinsInitialJobsList = new LinkedList<>();
        Job dummy = new Job("VeryImportentProject","red");
        Job dummy1 = new Job("AlphaCentauri","blue");
        Job dummy2 = new Job("Manhattan","green");
        jenkinsInitialJobsList.add(dummy);
        jenkinsInitialJobsList.add(dummy1);
        jenkinsInitialJobsList.add(dummy2);

        initializeRecyclerView(jenkinsInitialJobsList);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                return true;
            default:
                break;
        }
        return false;
    }


    private void initializeRecyclerView(List<Job> jenkinsInitialJobsList){
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAdapter = new JobsRecyclerViewAdapter(jenkinsInitialJobsList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}
