package application.port;

import domain.model.RE;
import infrastructure.api.REFetchException;

import java.util.List;

public interface REFeederInterface {
    List<RE> fetchEnergyData() throws REFetchException;
}
