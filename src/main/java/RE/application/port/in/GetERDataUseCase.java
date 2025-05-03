package RE.application.port.in;

import RE.domain.model.REData;
import RE.infrastructure.out.api.REDataFetchException;

import java.util.List;

public interface GetERDataUseCase {
    List<REData> execute() throws REDataFetchException;
}
