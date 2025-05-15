package org.ulpgc.dacd.enerweather.reFeeder.infrastructure.rest;

import com.google.gson.Gson;
import org.ulpgc.dacd.enerweather.reFeeder.application.port.ReeRepositoryPort;
import org.ulpgc.dacd.enerweather.reFeeder.application.service.ReeService;
import org.ulpgc.dacd.enerweather.reFeeder.domain.model.RE;

import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.accessors.ReeFetchException;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.persistence.MessagePublisher;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ReeController {
    private final ReeService service;
    private final ReeRepositoryPort repository;


    public ReeController(ReeService service, ReeRepositoryPort repository) {
        this.service = service;
        this.repository = repository;
    }

    public  void execute() {
        try {
            List<RE> dataList = service.getEnergyData();
            repository.saveAll(dataList);

            try (MessagePublisher publisher = new MessagePublisher()) {
                Gson gson = new Gson();
                for (RE re : dataList) {
                    String json = gson.toJson(re);
                    publisher.publish(json);
                    System.out.println("Published: " + json);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

        } catch (ReeFetchException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public void startPeriodicTask(long intervalSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::execute, 0, intervalSeconds, TimeUnit.SECONDS);

    }
}

