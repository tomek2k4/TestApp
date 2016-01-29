package com.example.tmaslon.testapp.service;

import android.util.Log;

import com.example.tmaslon.testapp.JenkinsClientApplication;
import com.example.tmaslon.testapp.exceptions.UserNotDefinedException;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.OkHeaders;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit.http.HTTP;


/**
 * Created by tmaslon on 2016-01-27.
 */
public class AuthenticationInterceptor implements Interceptor {

    boolean autenticated = false;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.d(JenkinsClientApplication.TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response;

        try {
            final String credentials = JenkinsClientApplication.getInstance().getKeyManager().read();
            Request newRequest = request.newBuilder().addHeader("Authorization","Basic "+credentials).build();
            response = chain.proceed(newRequest);
        } catch (UserNotDefinedException e) {
            Log.e(JenkinsClientApplication.TAG,e.getMessage());
            e.printStackTrace();
            response = chain.proceed(request);
        }

        if(response.code()== HttpsURLConnection.HTTP_OK){
            setAutenticated(true);
        }else {
            setAutenticated(false);
        }

        long t2 = System.nanoTime();
        Log.d(JenkinsClientApplication.TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }

    public boolean isAutenticated() {
        return autenticated;
    }

    private void setAutenticated(boolean autenticated) {
        this.autenticated = autenticated;
    }

}
