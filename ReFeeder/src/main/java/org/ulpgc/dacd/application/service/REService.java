package org.ulpgc.dacd.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ulpgc.dacd.application.port.GetERUseCase;
import org.ulpgc.dacd.application.port.RERepositoryPort;
import org.ulpgc.dacd.application.port.REFeederInterface;
import org.ulpgc.dacd.domain.model.RE;
import org.ulpgc.dacd.infrastructure.accessors.REFetchException;
import org.ulpgc.dacd.infrastructure.messaging.MessagePublisher;
import com.google.gson.Gson;

import javax.jms.JMSException;
import java.util.List;

public class REService implements GetERUseCase {
    private static final Logger log = LoggerFactory.getLogger(REService.class);
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

            try(MessagePublisher publisher = new MessagePublisher()) {
                Gson gson = new Gson();
                for (RE re : batch) {
                    String json = gson.toJson(re);
                    publisher.publish(json);
                }
            } catch (JMSException e) {
                log.error("Failed to publish events for {} records", batch.size(), e);
            }
            return batch;
        } catch (REFetchException e) {
            System.out.println("Error: " + e.getMessage());
            return List.of();
        }
    }
}
