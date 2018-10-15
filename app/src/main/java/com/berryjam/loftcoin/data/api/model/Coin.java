package com.berryjam.loftcoin.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Coin {
    public int id;
    public String name;
    public  String symbol;
    public int rank;

    @SerializedName("website_slug")
    public String slug;

    @SerializedName("last_updated")
    public long updated;

    public Map<String, Quote> quotes;

}
