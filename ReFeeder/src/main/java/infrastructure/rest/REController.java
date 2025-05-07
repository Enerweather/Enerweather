package infrastructure.rest;

import application.port.GetERUseCase;
import domain.model.RE;

import infrastructure.api.REFetchException;
import java.util.List;


public class REController {
    private final GetERUseCase useCase;

    public REController(GetERUseCase useCase) {

        this.useCase = useCase;
    }

    public List<RE> getEnergyData() throws REFetchException {
        return useCase.execute();
    }
}
