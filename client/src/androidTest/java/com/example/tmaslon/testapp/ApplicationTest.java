package com.example.tmaslon.testapp;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.junit.Test;

import static org.junit.Assert.assertThat;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);


    }

    @Test
    public void test(){
        assertTrue(false);
    }
}