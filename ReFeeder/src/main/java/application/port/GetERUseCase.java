package application.port;

import domain.model.RE;
import infrastructure.api.REFetchException;

import java.util.List;

public interface GetERUseCase {
    List<RE> execute() throws REFetchException;
}
