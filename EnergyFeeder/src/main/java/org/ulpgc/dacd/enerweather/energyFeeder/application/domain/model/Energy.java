package org.ulpgc.dacd.enerweather.energyFeeder.application.domain.model;

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
}
