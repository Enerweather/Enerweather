package org.ulpgc.dacd.infrastructure.rest;

import org.ulpgc.dacd.application.port.GetERUseCase;
import org.ulpgc.dacd.domain.model.RE;

import org.ulpgc.dacd.infrastructure.accessors.REFetchException;
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
