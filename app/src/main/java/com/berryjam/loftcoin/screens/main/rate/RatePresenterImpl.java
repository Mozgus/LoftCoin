package com.berryjam.loftcoin.screens.main.rate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.berryjam.loftcoin.data.api.Api;
import com.berryjam.loftcoin.data.api.model.Coin;
import com.berryjam.loftcoin.data.api.model.RateResponse;
import com.berryjam.loftcoin.data.db.Database;
import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.db.model.CoinEntityMapper;
import com.berryjam.loftcoin.data.prefs.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatePresenterImpl implements RatePresenter {

    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;

    @Nullable
    private RateView view;

    RatePresenterImpl(Api api, Prefs prefs, Database database, CoinEntityMapper mapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = mapper;
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
        List<CoinEntity> coins = database.getCoins();
        if (null != view) {
            view.setCoins(coins);
        }
    }

    @Override
    public void onRefresh() {
        loadRate();
    }

    private void loadRate() {
        api.ticker("array", prefs.getFiatCurrency().name()).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(@NonNull Call<RateResponse> call, @NonNull Response<RateResponse> response) {
                if (null != response.body()) {
                    List<Coin> coins = response.body().data;
                    List<CoinEntity> entities = mapper.mapCoins(coins);
                    database.saveCoins(entities);
                    if (null != view) {
                        view.setCoins(entities);
                    }
                }
                if (null != view) {
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

}
