package com.berryjam.loftcoin.screens.currencies;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berryjam.loftcoin.R;
import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.model.Currency;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.CurrencyViewHolder> {

    private List<CoinEntity> coins = Collections.emptyList();

    @Nullable
    private CurrenciesAdapterListener listener;

    public void setCoins(List<CoinEntity> coins) {
        this.coins = coins;
        notifyDataSetChanged();
    }

    void setListener(@NonNull CurrenciesAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        holder.bind(coins.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.symbol_text)
        TextView symbolText;
        @BindView(R.id.symbol_icon)
        ImageView symbolIcon;
        @BindView(R.id.currency_name)
        TextView name;

        CurrencyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(CoinEntity coin, CurrenciesAdapterListener listener) {
            bindIcon(coin);
            bindName(coin);

            itemView.setOnClickListener(v -> {
                if (null != listener) {
                    listener.onCurrencyClick(coin);
                }
            });
        }

        private void bindIcon(CoinEntity coin) {
            Currency currency = Currency.getCurrency(coin.symbol);
            if (null != currency) {
                symbolIcon.setVisibility(View.VISIBLE);
                symbolText.setVisibility(View.GONE);
                symbolIcon.setImageResource(currency.iconRes);
            } else {
                symbolIcon.setVisibility(View.GONE);
                symbolText.setVisibility(View.VISIBLE);
                symbolText.setText(String.valueOf(coin.symbol.charAt(0)));
            }
        }

        private void bindName(CoinEntity coin) {
            name.setText(itemView.getContext()
                    .getString(R.string.currencies_bottom_sheet_currency_name, coin.name, coin.symbol));
        }

    }

}
