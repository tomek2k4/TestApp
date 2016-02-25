package com.example.tmaslon.testapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import junit.framework.TestCase;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by tomasz on 25.02.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class KeyManagerTest{

    private static final String KEY_PREFS = "key_preferences";
    private static final String PREFS = "prefs";


    private static final String FAKE_KEY_1 = "123456789";
    private static final String FAKE_KEY_2 = "b515ce85-f796-415f-a24a-b681b4da85a1";

    @Mock
    Context mockedContext;

    @Mock
    SharedPreferences.Editor mEditor;

    @Before
    public void setup(){
        initMocks(this);

    }


    @Test
    public void testSaveKeys(){

        SharedPreferences mockedSharedPreference = Mockito.mock(SharedPreferences.class);
        Mockito.when(mockedContext.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockedSharedPreference);

        SharedPreferences.Editor mockedEditor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(mockedSharedPreference.edit()).thenReturn(mockedEditor);

        Mockito.when(mockedEditor.putString(Mockito.anyString(),Mockito.anyString())).thenReturn(mockedEditor);

        mockedSharedPreference.getString(KEY_PREFS, "");
        mockedContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        mockedSharedPreference.edit().putString(KEY_PREFS, FAKE_KEY_1).apply();

//        Mockito.when(mockedSharedPreference.contains(Mockito.anyString())).thenReturn(true);
//        Mockito.when(mockedSharedPreference.getLong(Mockito.anyString(), Mockito.anyLong())).thenReturn(122l);
//        Assert.assertEquals(122l, dataUtility.getStoredTime());

//        // Given a mocked Context injected into the object under test...
//        when(context.getSharedPreferences(PREFS,Context.MODE_PRIVATE))
//                .thenReturn(mSharedPreferences);
//
//        KeyManager keyManager = new KeyManager(mockedContext);
//        Whitebox.setInternalState(keyManager, "sharedKeyPrefs", mockedSharedPreference);
//
//
//        keyManager.save(FAKE_KEY_1);
//
//
//        assertThat(context.getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("key_preferences",""),
//                is(FAKE_KEY_1));
    }

}
