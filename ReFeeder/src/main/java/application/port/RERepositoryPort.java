package application.port;

import domain.model.RE;

import java.util.List;
import java.util.Optional;

public interface RERepositoryPort {
    void saveAll(List<RE> batch);
    Optional<RE> findLatestByIndicator(String indicator);
}
