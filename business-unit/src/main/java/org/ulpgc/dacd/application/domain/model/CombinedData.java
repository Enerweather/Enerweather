package org.ulpgc.dacd.application.domain.model;

public class CombinedData {
    private final WeatherData weather;
    private final EnergyData energy;

    public CombinedData(WeatherData weather, EnergyData energy) {
        this.weather = weather;
        this.energy = energy;
    }

    public WeatherData getWeather() { return weather; }
    public EnergyData getEnergy() { return energy; }
}
