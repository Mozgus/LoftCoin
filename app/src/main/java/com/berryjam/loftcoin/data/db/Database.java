package com.berryjam.loftcoin.data.db;

import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.db.model.Transaction;
import com.berryjam.loftcoin.data.db.model.TransactionModel;
import com.berryjam.loftcoin.data.db.model.Wallet;
import com.berryjam.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {
    void saveCoins(List<CoinEntity> coins);

    Flowable<List<CoinEntity>> getCoins();

    void saveWallet(Wallet wallet);

    Flowable<List<WalletModel>> getWallets();

    void saveTransactions(List<Transaction> transactions);

    Flowable<List<TransactionModel>> getTransactions(String walletId);

    CoinEntity getCoin(String symbol);

}
