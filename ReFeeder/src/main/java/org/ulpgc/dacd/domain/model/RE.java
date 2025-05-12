package org.ulpgc.dacd.domain.model;

public class RE {
    private String indicator;
    private double value;
    private double percentage;
    private String unit;
    private String timestamp;
    private String geoName;
    private int geoId;


    public String getIndicator() { return indicator; }
    public void setIndicator(String indicator) { this.indicator = indicator; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getGeoName() { return geoName; }
    public void setGeoName(String geoName) { this.geoName = geoName; }
    public int getGeoId() { return geoId; }
    public void setGeoId(int geoId) { this.geoId = geoId; }
}
