package com.example.tmaslon.testapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.MainActivity;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.listadapter.DividerItemDecoration;
import com.example.tmaslon.testapp.listadapter.ItemClickSupport;
import com.example.tmaslon.testapp.listadapter.JobsRecyclerViewAdapter;
import com.example.tmaslon.testapp.model.Job;
import com.example.tmaslon.testapp.model.JobsListProvider;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tmaslon on 2016-01-26.
 */
public class JobListFragment extends Fragment {

    @InjectView(R.id.jobs_list_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;


    @InjectView(R.id.jenkins_jobs_recycler_view)
    RecyclerView recyclerView;

    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private MainActivity mainActivity;
    private List<Job> jenkinsInitialJobsList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try{
            jenkinsInitialJobsList = ((JobsListProvider)getArguments().getSerializable("jobs_list_provider")).getJobs();
        }catch (NullPointerException e){
            Log.e(JenkinsClientApplication.TAG,"There were no argument passed");
        }

        initializeRecyclerView(jenkinsInitialJobsList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Snackbar.make(getView(), mainActivity.getString(R.string.logging_out_string), Snackbar.LENGTH_LONG).show();
                jenkinsInitialJobsList = null;
                mainActivity.logout();
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
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.d(JenkinsClientApplication.TAG, "Clicked on jobs list item");
            }
        });

        recyclerViewAdapter = new JobsRecyclerViewAdapter(jenkinsInitialJobsList);
        recyclerView.setAdapter(recyclerViewAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(JenkinsClientApplication.TAG,"Swipe refresh occured");
                refreshItems();
            }
        });
    }

    private void refreshItems() {
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
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
