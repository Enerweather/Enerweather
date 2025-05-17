package org.ulpgc.dacd.infrastructure.port;

import org.ulpgc.dacd.application.domain.model.EnergyData;
import org.ulpgc.dacd.application.domain.model.WeatherData;

import java.util.Optional;

public interface DatamartQueryPort {
    Optional<WeatherData> getLatestWeatherData(String city) ;
    Optional<EnergyData> getLatestEnergy(String indicator);
}
