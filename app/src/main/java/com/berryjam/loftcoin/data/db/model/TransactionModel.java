package com.berryjam.loftcoin.data.db.model;

import android.arch.persistence.room.Embedded;

public class TransactionModel {

    @Embedded()
    public Transaction transaction;
    @Embedded()
    public CoinEntity coin;

}
