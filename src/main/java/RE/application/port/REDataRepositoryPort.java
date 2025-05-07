package RE.application.port;

import RE.domain.model.REData;

import java.util.List;
import java.util.Optional;

public interface REDataRepositoryPort {
    void saveAll(List<REData> batch);
    Optional<REData> findLatestByIndicator(String indicator);
}
