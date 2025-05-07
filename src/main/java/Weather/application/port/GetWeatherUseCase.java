package Weather.application.port;
import Weather.domain.model.WeatherData;
public interface GetWeatherUseCase {
    WeatherData execute(String city);
}
