package org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port;

import org.ulpgc.dacd.enerweather.weatherFeeder.application.domain.model.Weather;
import java.util.Optional;
public interface WeatherRepositoryPort {
    void save(Weather data);
    Optional<Weather> findLatest(String city);
}
