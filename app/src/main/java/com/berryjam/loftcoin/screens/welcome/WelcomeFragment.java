package com.berryjam.loftcoin.screens.welcome;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berryjam.loftcoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeFragment extends Fragment {

    private static final String KEY_PAGE = "PAGE";

    public static WelcomeFragment newInstance(WelcomePage page) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_PAGE, page);
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subtitle)
    TextView subtitle;

    private WelcomePage page;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            page = getArguments().getParcelable(KEY_PAGE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        title.setText(page.titleId);
        subtitle.setText(page.subtitleId);
        icon.setImageResource(page.iconId);

    }
}
