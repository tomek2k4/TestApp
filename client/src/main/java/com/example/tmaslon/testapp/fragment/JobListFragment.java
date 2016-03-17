package com.example.tmaslon.testapp.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.MainActivity;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.data.JobsContract;
import com.example.tmaslon.testapp.listadapter.DividerItemDecoration;
import com.example.tmaslon.testapp.listadapter.ItemClickSupport;
import com.example.tmaslon.testapp.listadapter.JobsRecyclerViewAdapter;
import com.example.tmaslon.testapp.model.Job;
import com.example.tmaslon.testapp.model.JobsListProvider;
import com.example.tmaslon.testapp.service.RefreshService;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tmaslon on 2016-01-26.
 */
public class JobListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @InjectView(R.id.jobs_list_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @InjectView(R.id.jenkins_jobs_recycler_view)
    RecyclerView recyclerView;

    @InjectView(R.id.empty_view)
    TextView emptyView;

    List<Job> listJob = null;

    private JobsRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(JenkinsClientApplication.TAG, "JobListFragment onCreate()");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

//        Bundle bundle = this.getArguments();
//
//        JobsListProvider jobsListProvider = (JobsListProvider) bundle.getSerializable(MainActivity.JOBS_LIST_PROVIDER);
//        listJob = jobsListProvider.getJobs();
//
//        Log.d(JenkinsClientApplication.TAG,"List of jobs:");
//        for(Job job: listJob){
//            Log.d(JenkinsClientApplication.TAG,job.getName());
//        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(JenkinsClientApplication.TAG,"JobListFragment onCreateView()");
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(JenkinsClientApplication.TAG, "JobListFragment onViewCreated()");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(JenkinsClientApplication.TAG, "JobListFragment onActivityCreated()");
        mainActivity = (MainActivity)getActivity();

        initializeRecyclerView();


        getLoaderManager().initLoader(0, null, this);

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
                mainActivity.logout();
                return true;
            default:
                break;
        }
        return false;
    }


    private void initializeRecyclerView(){
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

        recyclerViewAdapter = new JobsRecyclerViewAdapter(mainActivity);
        recyclerView.setAdapter(recyclerViewAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(JenkinsClientApplication.TAG,"Swipe refresh occured");
                refreshItems();
            }
        });


        swipeRefreshLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

    }

    private void refreshItems() {
        mainActivity.startService(new Intent(mainActivity, RefreshService.class));
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Log.d(JenkinsClientApplication.TAG,"Created CursorLoader");
        return new CursorLoader(getActivity(), JobsContract.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(JenkinsClientApplication.TAG, "Loader onLoadFinished()");
        recyclerViewAdapter.swapCursor(cursor);
        if(cursor.getCount()!=0){
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }else{
            emptyView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            emptyView.setText(mainActivity.getString(R.string.no_data_to_list_string));
            Toast.makeText(mainActivity,mainActivity.getString(R.string.no_data_to_list_string),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerViewAdapter.swapCursor(null);
        swipeRefreshLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }
}
