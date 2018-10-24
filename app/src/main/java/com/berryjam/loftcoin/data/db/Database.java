package com.berryjam.loftcoin.data.db;

import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.db.model.Wallet;
import com.berryjam.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {
    void saveCoins(List<CoinEntity> coins);

    Flowable<List<CoinEntity>> getCoins();

    void saveWallet(Wallet wallet);

    Flowable<List<WalletModel>> getWallets();

    CoinEntity getCoin(String symbol);

}
