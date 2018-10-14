package com.berryjam.loftcoin.screens.launch;

import android.app.Activity;
import android.os.Bundle;

import com.berryjam.loftcoin.App;
import com.berryjam.loftcoin.data.prefs.Prefs;
import com.berryjam.loftcoin.screens.start.StartActivity;
import com.berryjam.loftcoin.screens.welcome.WelcomeActivity;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Prefs prefs = ((App) getApplication()).getPrefs();
        if (prefs.isFirstLaunch()) {
            WelcomeActivity.startInNewTask(this);
        } else {
            StartActivity.startInNewTask(this);
        }
    }

}
