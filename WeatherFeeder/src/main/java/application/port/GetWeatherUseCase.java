package application.port;
import domain.model.Weather;
public interface GetWeatherUseCase {
    Weather execute(String city);
}
