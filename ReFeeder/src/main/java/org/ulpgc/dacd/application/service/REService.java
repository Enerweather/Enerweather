package org.ulpgc.dacd.application.service;

import org.ulpgc.dacd.application.port.EventPublisher;
import org.ulpgc.dacd.application.port.GetERUseCase;
import org.ulpgc.dacd.application.port.RERepositoryPort;
import org.ulpgc.dacd.application.port.REFeederInterface;
import org.ulpgc.dacd.domain.model.RE;
import org.ulpgc.dacd.infrastructure.accessors.REFetchException;
import org.ulpgc.dacd.infrastructure.messaging.MessagePublisher;
import com.google.gson.Gson;

import java.util.List;

public class REService implements GetERUseCase {
    private final REFeederInterface feeder;
    private final RERepositoryPort repository;

    public REService(REFeederInterface feeder, RERepositoryPort repository) {
        this.feeder = feeder;
        this.repository = repository;
    }

    @Override
    public List<RE> execute() {
        try {
            List<RE> batch = feeder.fetchEnergyData();
            repository.saveAll(batch);

            try {
                Gson gson = new Gson();
                String json = gson.toJson(batch);

                EventPublisher publisher = new MessagePublisher();
                publisher.publish(json);
                publisher.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            return batch;
        } catch (REFetchException e) {
            System.out.println("Error: " + e.getMessage());
            return List.of();
        }
    }
}
