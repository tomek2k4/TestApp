package com.example.tmaslon.testapp;

import junit.framework.TestCase;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by tomasz on 25.02.2016.
 */
public class KeyManagerTestCase extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testSaveKeys(){
        String testString = new String("tomek");
        assertThat(testString.equals("tomek2"), is(true));
    }

}
