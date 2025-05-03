package RE.application.service;

import RE.application.port.in.GetERDataUseCase;
import RE.application.port.out.REFeeder;
import RE.domain.model.REData;
import RE.infrastructure.out.api.REDataFetchException;

import java.util.List;

public class REDataService implements GetERDataUseCase {
    private final REFeeder feeder;
    public REDataService(REFeeder feeder) {
        this.feeder = feeder;
    }

    @Override
    public List<REData> execute() throws REDataFetchException {
        return feeder.fetchEnergyData();
    }
}
