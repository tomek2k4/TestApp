package com.example.tmaslon.testapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.tmaslon.testapp.fragment.JobListFragment;

import static com.example.tmaslon.testapp.account.AccountUtils.ACCOUNT_TYPE;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //if(JenkinsClientApplication.getInstance().getKeyManager().isPresent()){
            getFragmentManager().beginTransaction().replace(R.id.content_main, new JobListFragment()).addToBackStack(null).commit();
       // }else {
       //     loadLoginFragment();
       // }

        AccountManager accountManager = AccountManager.get(this);

//        final AccountManagerFuture<Bundle> future = accountManager.getAuthTokenByFeatures(ACCOUNT_TYPE, authTokenType, null, this, null, null,
//                new AccountManagerCallback<Bundle>() {
//                    @Override
//                    public void run(AccountManagerFuture<Bundle> future) {
//                        Bundle bnd = null;
//                        try {
//                            bnd = future.getResult();
//                            authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
//                            if (authToken != null) {
//                                String accountName = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
//                                mConnectedAccount = new Account(accountName, AccountGeneral.ACCOUNT_TYPE);
//                                initButtonsAfterConnect();
//                            }
//                            showMessage(((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL"));
//                            Log.d("udinic", "GetTokenForAccount Bundle is " + bnd);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            showMessage(e.getMessage());
//                        }
//                    }
//                }
//                , null);



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
        //loadLoginFragment();
    }

//    private void loadLoginFragment(){
//        getFragmentManager().beginTransaction().replace(R.id.content_main, new LoginFragment()).commit();
//    }

}
