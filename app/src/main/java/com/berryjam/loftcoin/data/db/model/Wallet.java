package com.berryjam.loftcoin.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Wallet {

    @PrimaryKey
    @NonNull
    public String walletId; // id of our wallet

    public int currencyId; // id of our currency

    public double amount; // amount of money

    public Wallet(@NonNull String walletId, int currencyId, double amount) {
        this.walletId = walletId;
        this.currencyId = currencyId;
        this.amount = amount;
    }
}
