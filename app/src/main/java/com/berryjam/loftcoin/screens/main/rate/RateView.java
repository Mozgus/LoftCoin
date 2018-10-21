package com.berryjam.loftcoin.screens.main.rate;

import com.berryjam.loftcoin.data.db.model.CoinEntity;

import java.util.List;

public interface RateView {
    void setCoins(List<CoinEntity> coins);

    void setRefreshing(Boolean refreshing);

    void showCurrencyDialog();

    void showProgress();

    void hideProgress();
}
