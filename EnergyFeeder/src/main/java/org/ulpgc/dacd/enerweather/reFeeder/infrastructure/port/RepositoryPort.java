package org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port;

import org.ulpgc.dacd.enerweather.reFeeder.application.domain.model.Energy;

import java.util.List;
import java.util.Optional;

public interface RepositoryPort {
    void saveAll(List<Energy> batch);
    Optional<Energy> findLatest(String indicator);
}
