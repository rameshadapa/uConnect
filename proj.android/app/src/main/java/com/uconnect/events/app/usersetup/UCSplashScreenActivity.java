package com.uconnect.events.app.usersetup;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.uconnect.events.R;
import com.uconnect.events.utils.UCUtils;

/**
 * Created by jaggu on 4/18/2015.
 */
public class UCSplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucsplash);
        setupUser();
    }

    /**
     * Checking the user existance in local shared preferences. if user not exits
     * start setting up the user else showing the user home
     * screen (which contains contacts and events).
     */
    private void setupUser() {

        if(false) { /*User does not exists*/
           startSetupUser();
        } else {
            //1. Start service to reloading the user data.
            //2. Navigating to the user home screen.
        }
    }

    /**
     * Start setting up the user by reading email or phone no. and save the user
     * existence status true in local shared preferences and creating the local
     * database for user and crete the user on the server.
     */
    private void startSetupUser() {
        if(UCUtils.getUCUserIdFromDevice() != null) {
            //create the local db and also create the user on the server.
        } else {
            //Navigating to the custom user creation Activity.
        }
    }
}
