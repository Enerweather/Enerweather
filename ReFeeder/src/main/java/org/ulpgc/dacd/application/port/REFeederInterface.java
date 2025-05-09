package org.ulpgc.dacd.application.port;

import org.ulpgc.dacd.domain.model.RE;
import org.ulpgc.dacd.infrastructure.api.REFetchException;

import java.util.List;

public interface REFeederInterface {
    List<RE> fetchEnergyData() throws REFetchException;
}
