package com.example.tmaslon.testapp;

import android.app.Fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.example.tmaslon.testapp.fragment.JobListFragment;
import com.example.tmaslon.testapp.fragment.LoginFragment;
import com.example.tmaslon.testapp.model.JobsListProvider;

import java.io.IOException;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(JenkinsClientApplication.getInstance().getKeyManager().isPresent()){
            getFragmentManager().beginTransaction().replace(R.id.content_main, new JobListFragment()).addToBackStack(null).commit();
        }else {
            loadLoginFragment();
        }
    }

    public void loggedIn(){

        Fragment fragment = new JobListFragment();
        getFragmentManager()
                .beginTransaction().replace(R.id.content_main, fragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onBackPressed() {
        int  count = getFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            getFragmentManager().popBackStackImmediate();
        }else{
            super.onBackPressed();
        }
    }

    public void logout() {
        JenkinsClientApplication.getInstance().clearKeyManager();
        loadLoginFragment();
    }

    private void loadLoginFragment(){
        getFragmentManager().beginTransaction().replace(R.id.content_main, new LoginFragment()).commit();
    }

}
