package Weather.application.port.out;

import Weather.domain.model.WeatherData;

public interface WeatherFeeder {
    WeatherData fetchCurrentWeather(String location);
}
