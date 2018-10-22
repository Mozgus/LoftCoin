package com.berryjam.loftcoin.screens.currencies;

import com.berryjam.loftcoin.data.db.model.CoinEntity;

public interface CurrenciesAdapterListener {
    void onCurrencyClick(CoinEntity coin);
}
