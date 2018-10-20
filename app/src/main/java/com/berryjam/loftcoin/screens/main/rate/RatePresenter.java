package com.berryjam.loftcoin.screens.main.rate;

import com.berryjam.loftcoin.data.model.Fiat;

public interface RatePresenter {
    void attachView(RateView view);

    void detachView();

    void getRate();

    void onRefresh();

    void onMenuItemCurrencyClick();

    void onFiatCurrencySelected(Fiat currency);
}
