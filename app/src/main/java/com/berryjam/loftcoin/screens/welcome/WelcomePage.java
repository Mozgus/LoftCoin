package com.berryjam.loftcoin.screens.welcome;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class WelcomePage implements Parcelable {

    @DrawableRes
    int iconId;
    @StringRes
    int titleId;
    @StringRes
    int subtitleId;

    public WelcomePage(int iconId, int titleId, int subtitleId) {
        this.iconId = iconId;
        this.titleId = titleId;
        this.subtitleId = subtitleId;
    }

    private WelcomePage(Parcel in) {
        iconId = in.readInt();
        titleId = in.readInt();
        subtitleId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(iconId);
        dest.writeInt(titleId);
        dest.writeInt(subtitleId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WelcomePage> CREATOR = new Creator<WelcomePage>() {
        @Override
        public WelcomePage createFromParcel(Parcel in) {
            return new WelcomePage(in);
        }

        @Override
        public WelcomePage[] newArray(int size) {
            return new WelcomePage[size];
        }
    };

}
