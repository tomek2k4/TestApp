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

import com.example.tmaslon.testapp.BuildConfig;
import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.MainActivity;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.exceptions.UserNotAuthenticatedException;
import com.example.tmaslon.testapp.manager.KeyManager;
import com.example.tmaslon.testapp.model.JobsListProvider;
import com.example.tmaslon.testapp.service.AuthenticationInterceptor;
import com.example.tmaslon.testapp.service.JenkinsServiceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.inject(this, view);
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


        JenkinsClientApplication.getInstance().getJenkinsServiceManager().login(usernameString, passwordString, new Callback<JobsListProvider>() {
            @Override
            public void onResponse(Response<JobsListProvider> response, Retrofit retrofit) {
                enter.setEnabled(true);

                if(!BuildConfig.DEBUG){
                // if success then save the key
                    KeyManager keyManager = new KeyManager(JenkinsClientApplication.getInstance().getApplicationContext());
                    keyManager.save(KeyManager.encodeCredentialsForBasicAuthorization(usernameString, passwordString));
                    JenkinsClientApplication.getInstance().setKeyManager(keyManager);
                }

                Snackbar.make(getView(), mainActivity.getString(R.string.logged_in_as_string)+ usernameString, Snackbar.LENGTH_LONG).show();
                Log.d(JenkinsClientApplication.TAG,"Successfully logged into Jenkins server");

                mainActivity.loggedIn(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                enter.setEnabled(true);
                if (t instanceof UserNotAuthenticatedException){
                    Snackbar.make(getView(), mainActivity.getString(R.string.login_failed_string) + t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
                Log.d(JenkinsClientApplication.TAG, "Failed to log into Jenkins server or connection issue");
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity)getActivity();

        if(BuildConfig.DEBUG){
            InputStream is = null;
            // for debug purposes read the key from assets
            try {
                is = mainActivity.getResources().getAssets().open("secret.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String key = reader.readLine();
                KeyManager keyManager = new KeyManager(JenkinsClientApplication.getInstance().getApplicationContext());
                keyManager.save(key);
                JenkinsClientApplication.getInstance().setKeyManager(keyManager);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(is != null){
                    try {
                        is.close();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

}
