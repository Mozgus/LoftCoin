package com.berryjam.loftcoin.screens.welcome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.berryjam.loftcoin.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivityPagerAdapter extends FragmentPagerAdapter {

    private List<WelcomePage> pages;

    WelcomeActivityPagerAdapter(FragmentManager fm) {
        super(fm);

        pages = new ArrayList<>();
        pages.add(new WelcomePage(R.drawable.image_welcome_page_1, R.string.welcome_title_1, R.string.welcome_subtitle_1));
        pages.add(new WelcomePage(R.drawable.image_welcome_page_2, R.string.welcome_title_2, R.string.welcome_subtitle_2));
        pages.add(new WelcomePage(R.drawable.image_welcome_page_3, R.string.welcome_title_3, R.string.welcome_subtitle_3));
    }

    @Override
    public Fragment getItem(int position) {
        return WelcomeFragment.newInstance(pages.get(position));
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
