package RE.application.port;

import RE.domain.model.REData;
import RE.infrastructure.api.REDataFetchException;

import java.util.List;

public interface GetERDataUseCase {
    List<REData> execute() throws REDataFetchException;
}
