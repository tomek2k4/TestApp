package com.example.tmaslon.testapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.example.tmaslon.testapp.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().add(R.id.content_main, new LoginFragment()).addToBackStack(null).commit();
    }


}
