package com.berryjam.loftcoin.data.db.room;

import com.berryjam.loftcoin.data.db.Database;
import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.db.model.Transaction;
import com.berryjam.loftcoin.data.db.model.TransactionModel;
import com.berryjam.loftcoin.data.db.model.Wallet;
import com.berryjam.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public class DatabaseRoomImpl implements Database {
    private AppDatabase database;

    public DatabaseRoomImpl(AppDatabase database) {
        this.database = database;
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        database.coinDao().saveCoins(coins);
    }

    @Override
    public Flowable<List<CoinEntity>> getCoins() {
        return database.coinDao().getCoins();
    }

    @Override
    public void saveWallet(Wallet wallet) {
        database.walletDao().saveWallet(wallet);
    }

    @Override
    public Flowable<List<WalletModel>> getWallets() {
        return database.walletDao().getWallets();
    }

    @Override
    public void saveTransactions(List<Transaction> transactions) {
        database.walletDao().saveTransactions(transactions);
    }

    @Override
    public Flowable<List<TransactionModel>> getTransactions(String walletId) {
        return database.walletDao().getTransactions(walletId);
    }

    @Override
    public CoinEntity getCoin(String symbol) {
        return database.coinDao().getCoin(symbol);
    }

}
