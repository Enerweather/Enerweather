package Weather.application.port;

import Weather.domain.model.WeatherData;

public interface WeatherFeeder {
    WeatherData fetchCurrentWeather(String location);
}
