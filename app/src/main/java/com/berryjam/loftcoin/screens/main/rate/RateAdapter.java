package com.berryjam.loftcoin.screens.main.rate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berryjam.loftcoin.R;
import com.berryjam.loftcoin.data.api.model.Coin;
import com.berryjam.loftcoin.data.api.model.Quote;
import com.berryjam.loftcoin.data.model.Currency;
import com.berryjam.loftcoin.data.model.Fiat;
import com.berryjam.loftcoin.data.prefs.Prefs;
import com.berryjam.loftcoin.utils.CurrencyFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {
    private List<Coin> coins = Collections.emptyList();
    private Prefs prefs;

    RateAdapter(Prefs prefs) {
        this.prefs = prefs;
    }

    void setCoins(List<Coin> coins) {
        this.coins = coins;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RateAdapter.RateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rate, viewGroup, false);
        return new RateViewHolder(view, prefs);
    }

    @Override
    public void onBindViewHolder(@NonNull RateAdapter.RateViewHolder rateViewHolder, int position) {
        rateViewHolder.bind(coins.get(position), position);
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    public long getItemId(int position) {
        return coins.get(position).id;
    }

    static class RateViewHolder extends RecyclerView.ViewHolder {
        private static int[] colors = {
                0xFFF5FF30,
                0xFFFFFFFF,
                0xFF2ABDF5,
                0xFFFF7416,
                0xFF534FFF,
                0xFF4DFF88
        };

        @BindView(R.id.symbol_text)
        TextView symbolText;
        @BindView(R.id.symbol_icon)
        ImageView symbolIcon;
        @BindView(R.id.currency_name)
        TextView name;
        @BindView(R.id.percent_change)
        TextView percentChange;
        @BindView(R.id.price)
        TextView price;

        private Random random = new Random();
        private Context context;
        private Prefs prefs;
        private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

        RateViewHolder(@NonNull View itemView, Prefs prefs) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            context = itemView.getContext();
            this.prefs = prefs;
        }

        void bind(Coin coin, int position) {
            bindIcon(coin);
            bindSymbol(coin);
            bindPrice(coin);
            bindPercentage(coin);
            bindBackground(position);
        }

        private void bindBackground(int position) {
            if (position % 2 == 0) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.rate_item_background_even));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.rate_item_background_odd));
            }
        }

        private void bindPercentage(Coin coin) {
            Quote quote = coin.quotes.get(prefs.getFiatCurrency().name());
            if (null != quote) {
                float percentChangeValue = quote.percentChange24h;
                percentChange.setText(context.getString(R.string.rate_item_percent_change, percentChangeValue));
                if (percentChangeValue >= 0) {
                    percentChange.setTextColor(context.getResources().getColor(R.color.percent_change_up));
                } else {
                    percentChange.setTextColor(context.getResources().getColor(R.color.percent_change_down));
                }
            }
        }

        private void bindPrice(Coin coin) {
            Fiat fiat = prefs.getFiatCurrency();
            Quote quote = coin.quotes.get(fiat.name());
            if (null != quote) {
                String value = currencyFormatter.format(quote.price, false);
                price.setText(context.getString(R.string.currency_amount, value, fiat.symbol));
            }
        }

        private void bindSymbol(Coin coin) {
            name.setText(coin.symbol);
        }

        private void bindIcon(Coin coin) {
            Currency currency = Currency.getCurrency(coin.symbol);
            if (currency != null) {
                symbolIcon.setVisibility(View.VISIBLE);
                symbolText.setVisibility(View.GONE);
                symbolIcon.setImageResource(currency.iconRes);
            } else {
                symbolIcon.setVisibility(View.GONE);
                symbolText.setVisibility(View.VISIBLE);

                Drawable background = symbolText.getBackground();
                Drawable wrapped = DrawableCompat.wrap(background);
                DrawableCompat.setTint(wrapped, colors[random.nextInt(colors.length)]);
                symbolText.setText(String.valueOf(coin.symbol.charAt(0)));
            }
        }

    }

}
