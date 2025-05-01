package RE.application.port.out;

import RE.domain.model.REData;
import RE.infrastructure.out.api.REDataFetchException;

import java.util.List;

public interface REFeeder {
    List<REData> fetchEnergyData() throws REDataFetchException;
}
