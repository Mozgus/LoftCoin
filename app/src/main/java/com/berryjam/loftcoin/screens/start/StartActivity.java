package com.berryjam.loftcoin.screens.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.berryjam.loftcoin.App;
import com.berryjam.loftcoin.R;
import com.berryjam.loftcoin.data.api.Api;
import com.berryjam.loftcoin.data.prefs.Prefs;
import com.berryjam.loftcoin.screens.main.MainActivity;

public class StartActivity extends AppCompatActivity implements StartView {

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, StartActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    private StartPresenter presenter;
    private Api api;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        api = ((App) getApplication()).getApi();
        prefs = ((App) getApplication()).getPrefs();
        presenter = new StartPresenterImpl(api, prefs);
        presenter.attachView(this);
        presenter.loadRate();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void navigateToMainScreen() {
        MainActivity.startInNewTask(this);
    }

}
