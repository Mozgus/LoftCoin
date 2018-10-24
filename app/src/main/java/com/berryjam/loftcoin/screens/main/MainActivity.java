package com.berryjam.loftcoin.screens.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.berryjam.loftcoin.R;
import com.berryjam.loftcoin.screens.main.converter.ConverterFragment;
import com.berryjam.loftcoin.screens.main.rate.RateFragment;
import com.berryjam.loftcoin.screens.main.wallets.WalletsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @BindView(R.id.bottom_navigation)
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(navigationListener);
        navigation.setOnNavigationItemReselectedListener(menuItem -> {
        });

        if (null == savedInstanceState) {
            navigation.setSelectedItemId(R.id.menu_item_rate);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_accounts:
                showWalletsFragment();
                break;
            case R.id.menu_item_rate:
                showRateFragment();
                break;
            case R.id.menu_item_converter:
                showConverterFragment();
                break;
        }
        return true;
    };

    private void showWalletsFragment() {
        WalletsFragment fragment = new WalletsFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void showRateFragment() {
        RateFragment fragment = new RateFragment();  // create a new fragment
        FragmentManager fm = getSupportFragmentManager();  //we get a fragment manager
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void showConverterFragment() {
        ConverterFragment fragment = new ConverterFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

}
