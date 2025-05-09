package org.ulpgc.dacd.application.port;

import org.ulpgc.dacd.domain.model.Weather;

public interface WeatherFeeder {
    Weather fetchCurrentWeather(String location);
}
