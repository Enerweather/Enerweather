package REDataApi.domain;

import REDataApi.infrastructure.api.REDataFetchException;

import java.util.List;

public interface REFeeder {
    List<REData> fetchEnergyData() throws REDataFetchException;
}
