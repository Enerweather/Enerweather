package RE.infrastructure.rest;

import RE.application.port.GetERDataUseCase;
import RE.domain.model.REData;

import RE.infrastructure.api.REDataFetchException;
import java.util.List;


public class REController {
    private final GetERDataUseCase useCase;

    public REController(GetERDataUseCase useCase) {

        this.useCase = useCase;
    }

    public List<REData> getEnergyData() throws REDataFetchException {
        return useCase.execute();
    }
}
