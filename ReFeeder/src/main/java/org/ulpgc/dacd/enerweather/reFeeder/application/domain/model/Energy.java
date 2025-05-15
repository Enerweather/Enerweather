package org.ulpgc.dacd.enerweather.reFeeder.application.domain.model;

public class Energy {
    private String indicator;
    private double value;
    private double percentage;
    private String unit;
    private String timestamp;
    private String geoName;
    private int geoId;

    public Energy(String indicator, double value, double percentage, String unit, String timestamp, String geoName, int geoId) {
        this.indicator = indicator;
        this.value = value;
        this.percentage = percentage;
        this.unit = unit;
        this.timestamp = timestamp;
        this.geoName = geoName;
        this.geoId = geoId;
    }

    public String getIndicator() { return indicator; }
    public double getValue() { return value; }
    public double getPercentage() { return percentage; }
    public String getUnit() { return unit; }
    public String getTimestamp() { return timestamp; }
    public String getGeoName() { return geoName; }
    public int getGeoId() { return geoId; }
}
