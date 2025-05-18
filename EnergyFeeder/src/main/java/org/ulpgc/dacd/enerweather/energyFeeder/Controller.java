package org.ulpgc.dacd.enerweather.energyFeeder;

import com.google.gson.Gson;
import org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.adapters.accessors.AccessorImp;
import org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.adapters.persistence.DBInitializer;
import org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.adapters.persistence.Repository;
import org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.port.Accessor;
import org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.port.RepositoryPort;
import org.ulpgc.dacd.enerweather.energyFeeder.application.domain.model.Energy;

import org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.adapters.accessors.FetchException;
import org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.adapters.persistence.MessagePublisher;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Controller {
    private final Accessor feeder;
    private final RepositoryPort repository;


    public Controller(String url) {
        DBInitializer.createEnergyTable();
        this.feeder = new AccessorImp(url);
        this.repository = new Repository();
    }

    public void execute() {
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

        } catch (FetchException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public void startPeriodicTask(long intervalInSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::execute, 0, intervalInSeconds, TimeUnit.SECONDS);

    }
}

