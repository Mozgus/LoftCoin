package com.berryjam.loftcoin.screens.start;

public interface StartPresenter {
    void attachView(StartView view);

    void detachView();

    void loadRate();
}
