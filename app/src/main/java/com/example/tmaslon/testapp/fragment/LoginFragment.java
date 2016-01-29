package com.example.tmaslon.testapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.manager.KeyManager;
import com.example.tmaslon.testapp.model.JobsListProvider;
import com.example.tmaslon.testapp.service.AuthenticationInterceptor;
import com.example.tmaslon.testapp.service.JenkinsServiceManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Created by tmaslon on 2016-01-26.
 */
public class LoginFragment extends Fragment {

    @InjectView(R.id.username)
    EditText username;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.enter)
    Button enter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.inject(this,view);
        return view;
    }

    @OnClick(R.id.enter)
    public void onEnter(){
        enter.setEnabled(false);
        final String usernameString = username.getText().toString();
        final String passwordString = password.getText().toString();

        if(usernameString.isEmpty()){
            username.setError(getResources().getString(R.string.user_string_error));
            enter.setEnabled(true);
            return;
        }

        if(passwordString.isEmpty()){
            password.setError(getResources().getString(R.string.password_string_error));
            enter.setEnabled(true);
            return;
        }

        KeyManager keyManager = new KeyManager(JenkinsClientApplication.getInstance().getApplicationContext());
        keyManager.save(encodeCredentialsForBasicAuthorization(usernameString, passwordString));
        JenkinsClientApplication.getInstance().setKeyManager(keyManager);

        new JenkinsServiceManager(getActivity()).login(usernameString, passwordString, new Callback<JobsListProvider>() {
            @Override
            public void onResponse(Response<JobsListProvider> response, Retrofit retrofit) {
                enter.setEnabled(true);
                Snackbar.make(getView(), "Logged in as: " + usernameString, Snackbar.LENGTH_LONG).show();
                Log.d(JenkinsClientApplication.TAG,"Successfully logged into Jenkins server");
            }

            @Override
            public void onFailure(Throwable t) {
                enter.setEnabled(true);
                JenkinsClientApplication.getInstance().clearKeyManager();
                Snackbar.make(getView(), "Login Failed. " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                Log.d(JenkinsClientApplication.TAG, "Failed to log into Jenkins server");
            }
        });
    }


    private String encodeCredentialsForBasicAuthorization(String username, String password){
        final String userAndPassword = username + ":" + password;
        return Base64.encodeToString(userAndPassword.getBytes(),Base64.NO_WRAP);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
