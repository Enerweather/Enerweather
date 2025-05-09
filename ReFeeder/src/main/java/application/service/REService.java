package application.service;

import application.port.GetERUseCase;
import application.port.RERepositoryPort;
import application.port.REFeederInterface;
import domain.model.RE;
import infrastructure.api.REFetchException;
import org.ulpgc.dacd.MessagePublisher;
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
                MessagePublisher publisher = new MessagePublisher("energy.topic");
                Gson gson = new Gson();
                for (RE data : batch) {
                    String json = gson.toJson(data);
                    publisher.sendMessage(json);
                }
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
