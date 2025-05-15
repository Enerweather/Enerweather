package org.ulpgc.dacd.enerweather.reFeeder;

import com.google.gson.Gson;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port.EnergyFeederInterface;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port.EnergyRepositoryPort;
import org.ulpgc.dacd.enerweather.reFeeder.application.domain.model.Energy;

import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.accessors.EnergyFetchException;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.persistence.MessagePublisher;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class EnergyController {
    private final EnergyFeederInterface feeder;
    private final EnergyRepositoryPort repository;


    public EnergyController(EnergyFeederInterface feeder, EnergyRepositoryPort repository) {
        this.feeder = feeder;
        this.repository = repository;
    }

    public  void execute() {
        try {
            List<Energy> dataList = feeder.fetchEnergyData();
            repository.saveAll(dataList);

            try (MessagePublisher publisher = new MessagePublisher()) {
                Gson gson = new Gson();
                for (Energy re : dataList) {
                    String json = gson.toJson(re);
                    publisher.publish(json);
                    System.out.println("Published: " + json);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

        } catch (EnergyFetchException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public void startPeriodicTask(long intervalSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::execute, 0, intervalSeconds, TimeUnit.SECONDS);

    }
}

