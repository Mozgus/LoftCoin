package com.berryjam.loftcoin.screens.main.wallets;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.berryjam.loftcoin.App;
import com.berryjam.loftcoin.R;
import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.prefs.Prefs;
import com.berryjam.loftcoin.screens.currencies.CurrenciesBottomSheet;
import com.berryjam.loftcoin.screens.currencies.CurrenciesBottomSheetListener;
import com.berryjam.loftcoin.screens.main.wallets.adapters.TransactionsAdapter;
import com.berryjam.loftcoin.screens.main.wallets.adapters.WalletsPagerAdapter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletsFragment extends Fragment implements CurrenciesBottomSheetListener {

    @BindView(R.id.wallets_toolbar)
    Toolbar toolbar;
    @BindView(R.id.wallets_pager)
    ViewPager walletsPager;
    @BindView(R.id.new_wallet)
    ViewGroup newWallet;
    @BindView(R.id.transactions_recycler)
    RecyclerView transactionsRecycler;

    private WalletsPagerAdapter walletsPagerAdapter;
    private TransactionsAdapter transactionsAdapter;
    private WalletsViewModel viewModel;

    public WalletsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(WalletsViewModelImpl.class);
        Prefs prefs = ((App) Objects.requireNonNull(getActivity()).getApplication()).getPrefs();
        walletsPagerAdapter = new WalletsPagerAdapter(prefs);
        transactionsAdapter = new TransactionsAdapter(prefs);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.accounts_screen_title);
        toolbar.inflateMenu(R.menu.menu_wallets);

        int screenWidth = getScreenWidth();
        int walletItemWidth = getResources().getDimensionPixelOffset(R.dimen.item_wallet_width);
        int walletItemMargin = getResources().getDimensionPixelOffset(R.dimen.item_wallet_margin);
        int pageMargin = (screenWidth - walletItemWidth) - walletItemMargin;

        walletsPager.setPageMargin(-pageMargin);
        walletsPager.setOffscreenPageLimit(5);
        walletsPager.setAdapter(walletsPagerAdapter);

        transactionsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        transactionsRecycler.setHasFixedSize(true);
        transactionsRecycler.setAdapter(transactionsAdapter);

        FragmentManager fragmentManager = getFragmentManager();
        if (null != fragmentManager) {
            Fragment bottomSheet = fragmentManager.findFragmentByTag(CurrenciesBottomSheet.TAG);
            if (null != bottomSheet) {
                ((CurrenciesBottomSheet) bottomSheet).setListener(this);
            }
        }

        viewModel.getWallets();

        initOutputs();
        initInputs();
    }

    private void initOutputs() {
        newWallet.setOnClickListener(view -> viewModel.onNewWalletClick());

        toolbar.getMenu().findItem(R.id.menu_item_add_wallet).setOnMenuItemClickListener(menuItem -> {
            viewModel.onNewWalletClick();
            return true;
        });
    }

    private void initInputs() {
        viewModel.transactions().observe(this, transactionModels ->
                transactionsAdapter.setTransactions(transactionModels));
        viewModel.wallets().observe(this, wallets ->
                walletsPagerAdapter.setWallets(wallets));
        viewModel.walletsVisible().observe(this, visible -> {
            if (null != visible) walletsPager.setVisibility(visible ? View.VISIBLE : View.GONE);
        });
        viewModel.newWalletVisible().observe(this, visible -> {
            if (null != visible) newWallet.setVisibility(visible ? View.VISIBLE : View.GONE);
        });
        viewModel.selectCurrency().observe(this, o ->
                showCurrenciesBottomSheet());
    }

    private void showCurrenciesBottomSheet() {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
        bottomSheet.show(Objects.requireNonNull(getFragmentManager()), CurrenciesBottomSheet.TAG);
        bottomSheet.setListener(this);
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        viewModel.onCurrencySelected(coin);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) Objects.requireNonNull(getContext()).getSystemService(Context.WINDOW_SERVICE);
        Display display = Objects.requireNonNull(wm).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

}
