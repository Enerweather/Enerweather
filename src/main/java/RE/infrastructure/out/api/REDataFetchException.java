package RE.infrastructure.out.api;

public class REDataFetchException extends Exception {
    public REDataFetchException(String message) {
        super(message);
    }
    public REDataFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
