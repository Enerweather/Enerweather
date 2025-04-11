package REDataApi.domain;

public class REData {
    private String indicator;
    private double value;
    private String unit;
    private String timestamp;
    private String geoName;
    private int geoId;

    //getter y setter
    public String getIndicator() { return indicator; }
    public void setIndicator(String indicator) { this.indicator = indicator; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getGeoName() { return geoName; }
    public void setGeoName(String geoName) { this.geoName = geoName; }
    public int getGeoId() { return geoId; }
    public void setGeoId(int geoId) { this.geoId = geoId; }
}
