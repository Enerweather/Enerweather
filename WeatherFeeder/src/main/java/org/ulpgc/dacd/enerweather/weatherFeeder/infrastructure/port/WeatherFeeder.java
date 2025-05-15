package org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port;

import org.ulpgc.dacd.enerweather.weatherFeeder.application.domain.model.Weather;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.accessors.FetchException;

public interface WeatherFeeder {
    Weather fetchCurrentWeather(String location) throws FetchException;
}
