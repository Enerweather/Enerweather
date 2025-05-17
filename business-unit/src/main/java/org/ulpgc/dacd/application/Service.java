package org.ulpgc.dacd.application;

import org.ulpgc.dacd.application.domain.model.CombinedData;
import org.ulpgc.dacd.application.domain.model.EnergyData;
import org.ulpgc.dacd.application.domain.model.WeatherData;
import org.ulpgc.dacd.infrastructure.port.DatamartQueryPort;
import org.ulpgc.dacd.infrastructure.port.HistoricalEventPort;

public class Service {
    private final DatamartQueryPort datamart;
    private final HistoricalEventPort events;

    public Service(DatamartQueryPort datamart, HistoricalEventPort events) {
        this.datamart = datamart;
        this.events = events;
    }

    public CombinedData getCombinedData(String city, String indicator) {
        WeatherData weather = datamart.getLatestWeatherData(city).orElse(new WeatherData(0.0, "no data", city));
        EnergyData energy = datamart.getLatestEnergy(indicator).orElse(new EnergyData(indicator, 0.0, "no data"));

        return new CombinedData(weather, energy);
    }
}
