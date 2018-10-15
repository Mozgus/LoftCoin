package com.berryjam.loftcoin.data.prefs;

import com.berryjam.loftcoin.data.model.Fiat;

public interface Prefs {
    void setFirstLaunch(boolean firstLaunch);

    boolean isFirstLaunch();

    void setFiatCurrency(Fiat currency);

    Fiat getFiatCurrency();
}
