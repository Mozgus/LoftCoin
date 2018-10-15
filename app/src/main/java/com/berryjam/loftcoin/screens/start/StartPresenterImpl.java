package com.berryjam.loftcoin.screens.start;

import android.support.annotation.Nullable;

import com.berryjam.loftcoin.data.prefs.Prefs;

class StartPresenterImpl implements StartPresenter {

    private final Prefs prefs;

    @Nullable
    private StartView view;

    public StartPresenterImpl(Prefs prefs) {
        this.prefs = prefs;
    }

    @Override
    public void attachView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadRate() {
    }

}
