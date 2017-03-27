package com.example.tmaslon.testapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tmaslon.testapp.account.KeyManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import junit.framework.Assert;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by tomasz on 25.02.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class KeyManagerTest{

    private static final String FAKE_KEY_1 = "123456789";
    private static final String FAKE_KEY_2 = "b515ce85-f796-415f-a24a-b681b4da85a1";

    @Mock
    Context mockedContext;

    @Mock
    SharedPreferences mockedSharedPreference;

    @Mock
    SharedPreferences.Editor mockedEditor;

    @Before
    public void setup(){
        initMocks(this);
    }


    @Test
    public void save_shouldStoreKeysInSharedPrefeferences(){

        Mockito.when(mockedContext.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockedSharedPreference);
        Mockito.when(mockedSharedPreference.edit()).thenReturn(mockedEditor);
        Mockito.when(mockedEditor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(mockedEditor);

        KeyManager keyManager = new KeyManager(mockedContext);
        keyManager.save(FAKE_KEY_1);

        Mockito.verify(mockedEditor).apply();
    }

    @Test
    public void save_shouldStoreKeyInModePrivate(){
        Mockito.when(mockedContext.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockedSharedPreference);
        Mockito.when(mockedSharedPreference.edit()).thenReturn(mockedEditor);
        Mockito.when(mockedEditor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(mockedEditor);

        KeyManager keyManager = new KeyManager(mockedContext);

        Mockito.verify(mockedContext).getSharedPreferences(KeyManager.PREFS, Context.MODE_PRIVATE);
    }

    @Test
    public void read_shouldGetStringFromSharedPreferences(){

        Mockito.when(mockedContext.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockedSharedPreference);
        Mockito.when(mockedSharedPreference.edit()).thenReturn(mockedEditor);
        Mockito.when(mockedEditor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(mockedEditor);
        Mockito.when(mockedSharedPreference.contains(Mockito.anyString())).thenReturn(true);
        Mockito.when(mockedSharedPreference.getString(Mockito.anyString(), Mockito.anyString())).thenReturn(FAKE_KEY_1);


        KeyManager keyManager = new KeyManager(mockedContext);
        keyManager.read();

        Mockito.verify(mockedSharedPreference).getString(KeyManager.KEY_PREFS, KeyManager.DEFAULT_KEY);
    }


    @Test
    public void read_shouldReturnKeyString(){

        Mockito.when(mockedContext.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockedSharedPreference);
        Mockito.when(mockedSharedPreference.edit()).thenReturn(mockedEditor);
        Mockito.when(mockedEditor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(mockedEditor);
        Mockito.when(mockedSharedPreference.contains(Mockito.anyString())).thenReturn(true);
        Mockito.when(mockedSharedPreference.getString(Mockito.anyString(), Mockito.anyString())).thenReturn(FAKE_KEY_1);

        KeyManager keyManager = new KeyManager(mockedContext);

        Assert.assertEquals(FAKE_KEY_1, keyManager.read());
    }

    @Test
    public void read_keyIsNotStored_returnDefaultKey(){
        Mockito.when(mockedContext.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockedSharedPreference);
        Mockito.when(mockedSharedPreference.contains(Mockito.anyString())).thenReturn(false);

        KeyManager keyManager = new KeyManager(mockedContext);

        Assert.assertEquals(KeyManager.DEFAULT_KEY,keyManager.read());

    }

}
