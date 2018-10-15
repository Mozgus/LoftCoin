package com.berryjam.loftcoin.screens.start;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.berryjam.loftcoin.data.api.Api;
import com.berryjam.loftcoin.data.api.model.RateResponse;
import com.berryjam.loftcoin.data.prefs.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class StartPresenterImpl implements StartPresenter {

    private static final String TAG = "StartPresenterImpl";

    private Api api;
    private Prefs prefs;

    @Nullable
    private StartView view;

    public StartPresenterImpl(Api api, Prefs prefs) {
        this.api = api;
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
        api.ticker("structure", prefs.getFiatCurrency().name()).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(@NonNull Call<RateResponse> call, @NonNull Response<RateResponse> response) {
                if (view != null) {
                    view.navigateToMainScreen();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RateResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: load rate error ", t);
            }
        });
    }

}
