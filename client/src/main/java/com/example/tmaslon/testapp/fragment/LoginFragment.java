package com.example.tmaslon.testapp.fragment;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.MainActivity;
import com.example.tmaslon.testapp.R;
import com.example.tmaslon.testapp.account.AccountUtils;
import com.example.tmaslon.testapp.exceptions.UserNotAuthenticatedException;
import com.example.tmaslon.testapp.account.KeyManager;
import com.squareup.okhttp.ResponseBody;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.example.tmaslon.testapp.account.AccountUtils.AUTHTOKEN_TYPE_FULL_ACCESS;


/**
 * Created by tmaslon on 2016-01-26.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

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

        JenkinsClientApplication.getInstance().getJenkinsServiceManager().login(usernameString, passwordString, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                enter.setEnabled(true);
                    KeyManager keyManager = new KeyManager(JenkinsClientApplication.getInstance().getApplicationContext());
                    keyManager.save(KeyManager.encodeCredentialsForBasicAuthorization(usernameString, passwordString));
                    JenkinsClientApplication.getInstance().setKeyManager(keyManager);

                Snackbar.make(getView(), mainActivity.getString(R.string.logged_in_as_string)+ usernameString, Snackbar.LENGTH_LONG).show();
                Log.d(TAG,"Successfully logged into Jenkins server");

                mainActivity.loggedIn();
            }

            @Override
            public void onFailure(Throwable t) {
                enter.setEnabled(true);
                if (t instanceof UserNotAuthenticatedException){
                    Snackbar.make(getView(), mainActivity.getString(R.string.login_failed_string) + t.getMessage(), Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make(getView(), mainActivity.getString(R.string.connecting_to_server_failed_string), Snackbar.LENGTH_LONG).show();
                }
                Log.d(TAG, "Failed to log into Jenkins server or connection issue");
            }
        });
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity)getActivity();


        AccountManager accountManager = AccountManager.get(mainActivity);

        final AccountManagerFuture<Bundle> future = accountManager.addAccount(AccountUtils.ACCOUNT_TYPE, AUTHTOKEN_TYPE_FULL_ACCESS, null, null, mainActivity, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    Log.d("udinic", "AddNewAccount Bundle is " + bnd);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);





    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

}
