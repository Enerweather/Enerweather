package application.port;

import domain.model.Weather;

public interface WeatherFeeder {
    Weather fetchCurrentWeather(String location);
}
