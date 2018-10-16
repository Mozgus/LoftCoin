package com.berryjam.loftcoin.screens.main.rate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.berryjam.loftcoin.data.api.Api;
import com.berryjam.loftcoin.data.api.model.RateResponse;
import com.berryjam.loftcoin.data.prefs.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatePresenterImpl implements RatePresenter {

    private Api api;
    private Prefs prefs;

    @Nullable
    private RateView view;

    public RatePresenterImpl(Api api, Prefs prefs) {
        this.api = api;
        this.prefs = prefs;
    }

    @Override
    public void attachView(RateView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getRate() {


        api.ticker("array", prefs.getFiatCurrency().name()).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(@NonNull Call<RateResponse> call, @NonNull Response<RateResponse> response) {
                if (null != view && null != response.body()) {
                    view.setCoins(response.body().data);
                    view.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RateResponse> call, @NonNull Throwable t) {
                if (view != null) {
                    view.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getRate();
    }

}
