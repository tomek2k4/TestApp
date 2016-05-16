package com.example.tmaslon.testapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.test.ViewAsserts.assertOnScreen;


/**
 * Created by tomasz on 06.03.2016.
 */
public class LoginFragmentInstrumentationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public static final String TEST_USERNAME = "tomek";
    private static final String TEST_PASSWORD = "hidstudent";
    private MainActivity mainActivity;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button buttonLogin;


    @SuppressWarnings( "deprecation" )
    public LoginFragmentInstrumentationTest() {
        super("com.example.tmaslon.testapp", MainActivity.class);
    }


    protected void setUp() throws Exception {
        super.setUp();

        mainActivity = getActivity();
        usernameEditText = (EditText) mainActivity.findViewById(R.id.username);
        passwordEditText = (EditText) mainActivity.findViewById(R.id.password);
        buttonLogin = (Button) mainActivity.findViewById(R.id.enter);
    }


    public void testUsernameEditTextExists(){
        assertOnScreen(mainActivity.getWindow().getDecorView(),usernameEditText);
    }

    public void testPaswordEditTextExists(){
        assertOnScreen(mainActivity.getWindow().getDecorView(),passwordEditText);
    }

    public void testButtonExistsWithLoginString() {
        assertOnScreen(mainActivity.getWindow().getDecorView(),buttonLogin);
        onView(withId(R.id.enter)).check(matches(withText("Login")));
    }

    public void testLoginFeature(){
        ViewActions.typeText(TEST_USERNAME);
        onView(withId(R.id.username)).perform(ViewActions.typeText(TEST_USERNAME));
        onView(withId(R.id.password)).perform(ViewActions.typeText(TEST_PASSWORD));
        onView(withId(R.id.enter)).perform(ViewActions.click());

    }


}
