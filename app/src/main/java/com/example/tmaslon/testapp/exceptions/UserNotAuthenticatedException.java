package com.example.tmaslon.testapp.exceptions;

/**
 * Created by tmaslon on 2016-01-29.
 */
public class UserNotAuthenticatedException extends Exception {

    public UserNotAuthenticatedException(String detailMessage) {
        super(detailMessage);
    }
}
