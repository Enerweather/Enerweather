package org.ulpgc.dacd.application.port;
import org.ulpgc.dacd.domain.model.Weather;
public interface GetWeatherUseCase {
    Weather execute(String city);
}
