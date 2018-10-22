package com.berryjam.loftcoin.screens.main.rate;

import android.support.annotation.Nullable;
import android.util.Log;

import com.berryjam.loftcoin.data.api.Api;
import com.berryjam.loftcoin.data.db.Database;
import com.berryjam.loftcoin.data.db.model.CoinEntityMapper;
import com.berryjam.loftcoin.data.model.Fiat;
import com.berryjam.loftcoin.data.prefs.Prefs;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RatePresenterImpl implements RatePresenter {
    private static final String TAG = "RatePresenterImpl";

    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;

    private CompositeDisposable disposables = new CompositeDisposable();

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
        disposables.dispose();
        this.view = null;
    }

    @Override
    public void getRate() {
        Disposable disposable = database.getCoins()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinEntities -> {
                            if (null != view) {
                                view.setCoins(coinEntities);
                            }
                        }, throwable -> {
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void onRefresh() {
        loadRate(true);
    }

    @Override
    public void onMenuItemCurrencyClick() {
        if (null != view) {
            view.showCurrencyDialog();
        }
    }

    @Override
    public void onFiatCurrencySelected(Fiat currency) {
        prefs.setFiatCurrency(currency); // save our currency in prefs
        loadRate(false);
    }

    private void loadRate(Boolean fromRefresh) {
        if (null != view && !fromRefresh) {
            view.showProgress();
        }
        Disposable disposable = api.ticker("array", prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> mapper.mapCoins(rateResponse.data))
                .map(coinEntities -> {
                    database.saveCoins(coinEntities);
                    return new Object();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object -> {
                            if (null != view) {
                                if (fromRefresh) {
                                    view.setRefreshing(false);
                                } else {
                                    view.hideProgress();
                                }
                            }
                        }, throwable -> {
                            Log.e(TAG, "loadRate: ", throwable);
                            if (null != view) {
                                if (fromRefresh) {
                                    view.setRefreshing(false);
                                } else {
                                    view.hideProgress();
                                }
                            }
                        }
                );
        disposables.add(disposable);
    }

}
