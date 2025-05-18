package org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port;

import org.ulpgc.dacd.enerweather.weatherFeeder.application.domain.model.Weather;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.accessors.FetchException;

public interface Accessor {
    Weather fetchCurrentWeather(String location) throws FetchException;
}
