package org.ulpgc.dacd.enerweather.weatherFeeder.application.port;

import org.ulpgc.dacd.enerweather.weatherFeeder.domain.model.Weather;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.accessors.WeatherFetchException;

public interface WeatherFeeder {
    Weather fetchCurrentWeather(String location) throws WeatherFetchException;
}
