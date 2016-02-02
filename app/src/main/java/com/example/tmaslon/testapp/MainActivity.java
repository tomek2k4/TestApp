package com.example.tmaslon.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.example.tmaslon.testapp.fragment.JobListFragment;
import com.example.tmaslon.testapp.fragment.LoginFragment;

import java.io.IOException;
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction().add(R.id.content_main, new LoginFragment()).addToBackStack(null).commit();

        Log.d(JenkinsClientApplication.TAG,"MainActivity onCreate() called.");
    }

    public void loggedIn(){
        getFragmentManager().beginTransaction().replace(R.id.content_main, new JobListFragment()).addToBackStack(null).commit();
    }


}
