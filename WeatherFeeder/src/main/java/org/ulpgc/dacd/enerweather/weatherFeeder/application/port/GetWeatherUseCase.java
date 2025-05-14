package org.ulpgc.dacd.enerweather.weatherFeeder.application.port;
import org.ulpgc.dacd.enerweather.weatherFeeder.domain.model.Weather;
public interface GetWeatherUseCase {
    Weather execute(String city);
}
