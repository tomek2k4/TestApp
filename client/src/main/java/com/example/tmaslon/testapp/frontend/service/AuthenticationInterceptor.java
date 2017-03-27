package com.example.tmaslon.testapp.frontend.service;

import android.util.Log;

import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.account.KeyManager;
import com.example.tmaslon.testapp.frontend.model.User;
import com.google.common.base.Optional;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by tmaslon on 2016-01-27.
 */
public class AuthenticationInterceptor implements Interceptor {

    private static final String TAG = AuthenticationInterceptor.class.getSimpleName();

    boolean authenticated = false;
    private User tempUser = null;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.d(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        String credentials = null;
        if(tempUser == null){
            // As an interceptor take credentials from key manager, it is not new login session
            Optional<KeyManager> keyManager = JenkinsClientApplication.getInstance().getKeyManager();
            if(keyManager.isPresent()){
                credentials = keyManager.get().read();
            }
        }else {
            //It is new login session
            credentials = KeyManager.encodeCredentialsForBasicAuthorization(tempUser.getUsername(),tempUser.getPassword());
            //clear the user we don't want to keep it in the field
            tempUser = null;
        }

        Response response;
        if(credentials!=null){
            // If credentials not null set authentication header
            Request newRequest = request.newBuilder().addHeader("Authorization","Basic "+credentials).build();
            response = chain.proceed(newRequest);
        }else {
            response = chain.proceed(request);
        }

        if(response.code()== HttpsURLConnection.HTTP_OK){
            setAuthenticated(true);
        }else {
            setAuthenticated(false);
        }

        long t2 = System.nanoTime();
        Log.d(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    private void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void setUser(User user) {
        this.tempUser = user;
    }
}
