package com.berryjam.loftcoin.screens.main.converter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.berryjam.loftcoin.App;
import com.berryjam.loftcoin.R;
import com.berryjam.loftcoin.data.db.Database;
import com.berryjam.loftcoin.data.db.model.CoinEntity;
import com.berryjam.loftcoin.data.model.Currency;
import com.berryjam.loftcoin.screens.currencies.CurrenciesBottomSheet;
import com.berryjam.loftcoin.screens.currencies.CurrenciesBottomSheetListener;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class ConverterFragment extends Fragment {
    private static final String SOURCE_CURRENCY_BOTTOM_SHEET_TAG = "source_currency_bottom_sheet";
    private static final String DESTINATION_CURRENCY_BOTTOM_SHEET_TAG = "destination_currency_bottom_sheet";
    private static final int[] colors = {
            0xFFF5FF30,
            0xFFFFFFFF,
            0xFF2ABDF5,
            0xFFFF7416,
            0xFFFF7416,
            0xFF534FFF,
    };

    @BindView(R.id.converter_toolbar)
    Toolbar toolbar;
    @BindView(R.id.source_currency)
    ViewGroup sourceCurrency;
    @BindView(R.id.source_amount)
    EditText sourceAmount;
    @BindView(R.id.destination_currency)
    ViewGroup destinationCurrency;
    @BindView(R.id.destination_amount)
    TextView destinationAmount;

    TextView sourceCurrencySymbolText;
    ImageView sourceCurrencySymbolIcon;
    TextView sourceCurrencySymbolName;
    TextView destinationCurrencySymbolText;
    ImageView destinationCurrencySymbolIcon;
    TextView destinationCurrencySymbolName;

    private ConverterViewModel viewModel;

    private Random random = new Random();
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        viewModel.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.converter_screen_title);

        Database database = ((App) Objects.requireNonNull(getActivity()).getApplication()).getDatabase();
        viewModel = new ConverterViewModelImpl(savedInstanceState, database);

        sourceCurrencySymbolText = sourceCurrency.findViewById(R.id.symbol_text);
        sourceCurrencySymbolIcon = sourceCurrency.findViewById(R.id.symbol_icon);
        sourceCurrencySymbolName = sourceCurrency.findViewById(R.id.currency_name);

        destinationCurrencySymbolText = destinationCurrency.findViewById(R.id.symbol_text);
        destinationCurrencySymbolIcon = destinationCurrency.findViewById(R.id.symbol_icon);
        destinationCurrencySymbolName = destinationCurrency.findViewById(R.id.currency_name);

        if (null == savedInstanceState) {
            sourceAmount.setText("1");
        }

        FragmentManager fragmentManager = getFragmentManager();
        if (null != fragmentManager) {
            Fragment bottomSheetSource = fragmentManager.findFragmentByTag(SOURCE_CURRENCY_BOTTOM_SHEET_TAG);
            if (null != bottomSheetSource) {
                ((CurrenciesBottomSheet) bottomSheetSource).setListener(sourceListener);
            }
            Fragment bottomSheetDestination = fragmentManager.findFragmentByTag(DESTINATION_CURRENCY_BOTTOM_SHEET_TAG);
            if (null != bottomSheetDestination) {
                ((CurrenciesBottomSheet) bottomSheetDestination).setListener(destinationListner);
            }
        }

        initOutputs();
        initInputs();
    }

    private void initOutputs() {
        disposables.add(RxTextView.afterTextChangeEvents(sourceAmount).subscribe(event ->
                viewModel.onSourceAmountChange(Objects.requireNonNull(event.editable()).toString())
        ));
        sourceCurrency.setOnClickListener(v ->
                viewModel.onSourceCurrencyClick()
        );
        destinationCurrency.setOnClickListener(v ->
                viewModel.onDestinationCurrencyClick()
        );
    }

    private void initInputs() {
        disposables.add(viewModel.sourceCurrency().subscribe(currency ->
                bindCurrency(currency, sourceCurrencySymbolIcon, sourceCurrencySymbolText, sourceCurrencySymbolName)
        ));
        disposables.add(viewModel.destinationCurrency().subscribe(currency ->
                bindCurrency(currency, destinationCurrencySymbolIcon, destinationCurrencySymbolText, destinationCurrencySymbolName)
        ));
        disposables.add(viewModel.destinationAmount().subscribe(s ->
                destinationAmount.setText(s)
        ));
        disposables.add(viewModel.selectSourceCurrency().subscribe(o ->
                showCurrenciesBottomSheet(true)
        ));

        disposables.add(viewModel.selectDestinationCurrency().subscribe(o ->
                showCurrenciesBottomSheet(false)
        ));
    }

    private void showCurrenciesBottomSheet(boolean source) {
        FragmentManager fragmentManager = getFragmentManager();
        if (null != fragmentManager) {
            CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
            if (source) {
                bottomSheet.show(fragmentManager, SOURCE_CURRENCY_BOTTOM_SHEET_TAG);
                bottomSheet.setListener(sourceListener);
            } else {
                bottomSheet.show(fragmentManager, DESTINATION_CURRENCY_BOTTOM_SHEET_TAG);
                bottomSheet.setListener(destinationListner);
            }
        }
    }

    private CurrenciesBottomSheetListener sourceListener = new CurrenciesBottomSheetListener() {
        @Override
        public void onCurrencySelected(CoinEntity coin) {
            viewModel.onSourceCurrencySelected(coin);
        }
    };

    private CurrenciesBottomSheetListener destinationListner = new CurrenciesBottomSheetListener() {
        @Override
        public void onCurrencySelected(CoinEntity coin) {
            viewModel.onDestinationCurrencySelected(coin);
        }
    };

    private void bindCurrency(String curr, ImageView symbolIcon, TextView symbolText, TextView currencyName) {
        Currency currency = Currency.getCurrency(curr);

        if (null != currency) {
            symbolIcon.setVisibility(View.VISIBLE);
            symbolText.setVisibility(View.GONE);
            symbolIcon.setImageResource(currency.iconRes);
        } else {
            symbolIcon.setVisibility(View.GONE);
            symbolText.setVisibility(View.VISIBLE);

            Drawable background = symbolText.getBackground();
            Drawable wrapped = DrawableCompat.wrap(background);
            DrawableCompat.setTint(wrapped, colors[random.nextInt(colors.length)]);

            symbolText.setText(String.valueOf(curr.charAt(0)));
        }
        currencyName.setText(curr);
    }

}
