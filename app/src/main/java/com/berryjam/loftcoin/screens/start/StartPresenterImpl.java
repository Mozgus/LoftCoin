package com.berryjam.loftcoin.screens.start;

import android.support.annotation.Nullable;
import android.util.Log;

import com.berryjam.loftcoin.data.api.Api;
import com.berryjam.loftcoin.data.api.model.Coin;
import com.berryjam.loftcoin.data.db.Database;
import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.db.model.CoinEntityMapper;
import com.berryjam.loftcoin.data.prefs.Prefs;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class StartPresenterImpl implements StartPresenter {

    private static final String TAG = "StartPresenterImpl";

    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    private StartView view;

    StartPresenterImpl(Api api, Prefs prefs, Database database, CoinEntityMapper entityMapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = entityMapper;
    }

    @Override
    public void attachView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.dispose();
        this.view = null;
    }

    @Override
    public void loadRate() {
        Disposable disposable = api.ticker("array", prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> {
                    List<Coin> coins = rateResponse.data;
                    List<CoinEntity> coinEntities = mapper.mapCoins(coins);
                    database.saveCoins(coinEntities);
                    return coinEntities;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinEntities -> {
                            if (null != view) {
                                view.navigateToMainScreen();
                            }
                        }, throwable -> Log.e(TAG, "startLoadRate: ", throwable)
                );
        disposables.add(disposable);
    }

}
