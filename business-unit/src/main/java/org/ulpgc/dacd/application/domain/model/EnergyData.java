package org.ulpgc.dacd.application.domain.model;

public class EnergyData {
    private String indicator;
    private double value;
    private String timestamp;


    public EnergyData(String indicator, double value, String timestamp) {
        this.indicator = indicator;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getIndicator() { return indicator; }
    public double getValue() { return value; }
    public String getTimestamp() { return timestamp; }
}
