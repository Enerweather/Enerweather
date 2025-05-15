package org.ulpgc.dacd.enerweather.reFeeder.application.port;

import org.ulpgc.dacd.enerweather.reFeeder.domain.model.RE;

import java.util.List;
import java.util.Optional;

public interface ReeRepositoryPort {
    void saveAll(List<RE> batch);
    Optional<RE> findLatestByIndicator(String indicator);
}
