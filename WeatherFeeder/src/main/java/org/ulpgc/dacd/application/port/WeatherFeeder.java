package org.ulpgc.dacd.application.port;

import org.ulpgc.dacd.domain.model.Weather;
import org.ulpgc.dacd.infrastructure.accessors.WeatherFetchException;

public interface WeatherFeeder {
    Weather fetchCurrentWeather(String location) throws WeatherFetchException;
}
