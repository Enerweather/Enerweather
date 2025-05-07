package application.port;
import domain.model.Weather;
import java.util.Optional;
public interface WeatherRepositoryPort {
    void save(Weather data);
    Optional<Weather> findLatest(String city);
}
