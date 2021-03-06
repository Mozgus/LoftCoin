package com.berryjam.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.berryjam.loftcoin.App;
import com.berryjam.loftcoin.data.db.Database;
import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.db.model.Transaction;
import com.berryjam.loftcoin.data.db.model.TransactionModel;
import com.berryjam.loftcoin.data.db.model.Wallet;
import com.berryjam.loftcoin.data.db.model.WalletModel;
import com.berryjam.loftcoin.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WalletsViewModelImpl extends WalletsViewModel {
    private static final String TAG = "WalletsViewModelImpl";

    private MutableLiveData<List<WalletModel>> walletsItems = new MutableLiveData<>();
    private MutableLiveData<Boolean> walletsVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> newWalletVisible = new MutableLiveData<>();
    private SingleLiveEvent<Object> selectCurrency = new SingleLiveEvent<>();
    private MutableLiveData<List<TransactionModel>> transactionsItems = new MutableLiveData<>();

    private Database database;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public LiveData<Boolean> walletsVisible() {
        return walletsVisible;
    }

    @Override
    public LiveData<Boolean> newWalletVisible() {
        return newWalletVisible;
    }

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }

    @Override
    public LiveData<List<TransactionModel>> transactions() {
        return transactionsItems;
    }

    @Override
    public LiveData<List<WalletModel>> wallets() {
        return walletsItems;
    }

    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);
        database = ((App) application).getDatabase();
    }

    @Override
    public void getWallets() {
        getWalletsInner();
    }

    private void getWalletsInner() {
        disposables.add(database.getWallets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wallets -> {
                    if (wallets.isEmpty()) {
                        walletsVisible.setValue(false);
                        newWalletVisible.setValue(true);
                    } else {
                        walletsVisible.setValue(true);
                        newWalletVisible.setValue(false);
                        if (null == walletsItems.getValue() || walletsItems.getValue().isEmpty()) {
                            WalletModel model = wallets.get(0);
                            String walletId = model.wallet.walletId;
                            getTransaction(walletId);
                        }
                        walletsItems.setValue(wallets);
                    }
                }));
    }

    private void getTransaction(String walletId) {
        disposables.add(database.getTransactions(walletId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions -> transactionsItems.setValue(transactions)));
    }

    @Override
    public void onNewWalletClick() {
        selectCurrency.postValue(new Object());
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        Wallet wallet = randomWallet(coin);
        List<Transaction> transactions = randomTransactions(wallet);
        disposables.add(
                Observable.fromCallable(() -> {
                    database.saveWallet(wallet);
                    database.saveTransactions(transactions);
                    return new Object();
                }).subscribeOn(Schedulers.io())
                        .subscribe());
    }

    @Override
    public void onWalletChanged(int position) {
        Wallet wallet = Objects.requireNonNull(walletsItems.getValue()).get(position).wallet;
        getTransaction(wallet.walletId);
    }

    private List<Transaction> randomTransactions(Wallet wallet) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            transactions.add(randomTransaction(wallet));
        }
        return transactions;
    }

    private Transaction randomTransaction(Wallet wallet) {
        Random random = new Random();
        long startDate = 148322880000L;
        long nowDate = System.currentTimeMillis();
        long date = startDate + (long) (random.nextDouble() * (nowDate - startDate));
        double amount = 2 * random.nextDouble();
        boolean amountSign = random.nextBoolean();
        return new Transaction(wallet.walletId, wallet.currencyId, amountSign ? amount : -amount, date);
    }

    private Wallet randomWallet(CoinEntity coin) {
        Random random = new Random();
        return new Wallet(UUID.randomUUID().toString(), coin.id, 10 * random.nextDouble());
    }

}
