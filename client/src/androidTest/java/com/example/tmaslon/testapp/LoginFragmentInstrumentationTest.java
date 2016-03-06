package com.example.tmaslon.testapp;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.test.ViewAsserts.assertOnScreen;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

/**
 * Created by tomasz on 06.03.2016.
 */
public class LoginFragmentInstrumentationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private EditText usernameEditText;
    private TextView mHelloWorldTextView;
    private Button buttonLogin;


    @SuppressWarnings( "deprecation" )
    public LoginFragmentInstrumentationTest() {
        super("com.example.tmaslon.testapp", MainActivity.class);
    }


    protected void setUp() throws Exception {
        super.setUp();

        mainActivity = getActivity();
        usernameEditText = (EditText) mainActivity.findViewById(R.id.username);
        buttonLogin = (Button) mainActivity.findViewById(R.id.enter);
    }


    public void testUsernameEditText(){
        assertOnScreen(mainActivity.getWindow().getDecorView(),usernameEditText);
    }

    public void testButton() {
        onView(withId(R.id.enter)).check(matches(withText("Login")));
    }
}
