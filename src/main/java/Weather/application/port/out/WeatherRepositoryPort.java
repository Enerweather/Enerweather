package Weather.application.port.out;
import Weather.domain.model.WeatherData;
import java.util.Optional;
public interface WeatherRepositoryPort {
    void save(WeatherData data);
    Optional<WeatherData> findLatest(String city);
}
