package org.ulpgc.dacd.enerweather.businessunit.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Energy {
    private final String indicator;
    private final double value;
    private final String timestamp;

    public Energy(String indicator, double value, String timestamp) {
        this.indicator = indicator;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getIndicator() { return indicator; }
    public double getValue() { return value; }
    public String getTimestamp() { return timestamp; }

    private static final Gson GSON = new GsonBuilder().create();
    public static Energy fromJson(String json) {
        return GSON.fromJson(json, Energy.class);
    }
}
