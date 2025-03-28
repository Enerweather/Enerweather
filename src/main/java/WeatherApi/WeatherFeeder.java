package WeatherApi;


public interface WeatherFeeder {
    WeatherData fetchCurrentWeather(String city);
}

