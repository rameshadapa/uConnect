/**
 * Created by jaggu on 4/18/2015.
 */
package com.uconnect.events.app.usersetup;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * UCCustomUserCreate is to create the user based on the user preference
 * by asking email or phone no.
 */
public class UCCustomUserCreate extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(0);

    }

    /**
     * In button click listener read the user input and validate it by
     * sending mail/message based on the input.
     * if its validates navigating to Custom user validate screen.
     */
    private void checkUserInput() {

    }
}
