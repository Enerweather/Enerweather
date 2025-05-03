package RE.application.service;

import RE.application.port.in.GetERDataUseCase;
import RE.application.port.out.REDataRepositoryPort;
import RE.application.port.out.REFeeder;
import RE.domain.model.REData;
import RE.infrastructure.out.api.REDataFetchException;

import java.util.List;

public class REDataService implements GetERDataUseCase {
    private final REFeeder feeder;
    private final REDataRepositoryPort repository;

    public REDataService(REFeeder feeder, REDataRepositoryPort repository) {
        this.feeder = feeder;
        this.repository = repository;
    }

    @Override
    public List<REData> execute() {
        try{
            List<REData> batch = feeder.fetchEnergyData();
            repository.saveAll(batch);
            return batch;
        } catch (REDataFetchException e) {
            System.out.println("Error: " + e.getMessage());
            return List.of();
        }
    }
}
