package Weather.application.port.in;
import Weather.domain.model.WeatherData;
public interface GetWeatherUseCase {
    WeatherData execute(String city);
}
