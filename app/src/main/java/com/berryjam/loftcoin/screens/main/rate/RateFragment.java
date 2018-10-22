package com.berryjam.loftcoin.screens.main.rate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.berryjam.loftcoin.App;
import com.berryjam.loftcoin.R;
import com.berryjam.loftcoin.data.api.Api;
import com.berryjam.loftcoin.data.db.Database;
import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.db.model.CoinEntityMapper;
import com.berryjam.loftcoin.data.model.Fiat;
import com.berryjam.loftcoin.data.prefs.Prefs;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RateFragment extends Fragment implements RateView,
        Toolbar.OnMenuItemClickListener, CurrencyDialog.CurrencyDialogListener {
    public static final String LAYOUT_MANAGER_STATE = "layout_manager_state";

    @BindView(R.id.rate_recycler)
    RecyclerView recycler;
    @BindView(R.id.rate_toolbar)
    Toolbar toolbar;
    @BindView(R.id.rate_refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.progress)
    ViewGroup progress;

    private RatePresenter presenter;
    private RateAdapter adapter;
    private Unbinder unbinder;
    private Parcelable layoutManagerState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        Api api = ((App) getActivity().getApplication()).getApi();
        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();
        Database database = ((App) activity.getApplication()).getDatabase();
        CoinEntityMapper mapper = new CoinEntityMapper();

        presenter = new RatePresenterImpl(api, prefs, database, mapper);
        adapter = new RateAdapter(prefs);
        adapter.setHasStableIds(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.rate_screen_title);
        toolbar.inflateMenu(R.menu.menu_rate);
        toolbar.setOnMenuItemClickListener(this);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        refresh.setOnRefreshListener(() -> presenter.onRefresh());

        FragmentManager fragmentManager = getFragmentManager();
        if (null != fragmentManager) {
            Fragment fragment = fragmentManager.findFragmentByTag(CurrencyDialog.TAG);
            if (null != fragment) {
                ((CurrencyDialog) fragment).setListener(this);
            }
        }

        presenter.attachView(this);
        presenter.getRate();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LAYOUT_MANAGER_STATE,
                Objects.requireNonNull(recycler.getLayoutManager()).onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void setCoins(List<CoinEntity> coins) {
        adapter.setCoins(coins);
        if (null != layoutManagerState) {
            Objects.requireNonNull(recycler.getLayoutManager()).onRestoreInstanceState(layoutManagerState);
            layoutManagerState = null;
        }
    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        refresh.setRefreshing(refreshing);
    }

    @Override
    public void showCurrencyDialog() {
        CurrencyDialog dialog = new CurrencyDialog();
        dialog.setListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        if (null != fragmentManager) {
            dialog.show(fragmentManager, CurrencyDialog.TAG);
        }
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_currency:
                presenter.onMenuItemCurrencyClick();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onCurrencySelected(Fiat currency) {
        presenter.onFiatCurrencySelected(currency);
    }

}
