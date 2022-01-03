package exception;

public class WireMockException extends Exception {

    public WireMockException(String message) {
        super(message);
    }


    public WireMockException(String message, Throwable cause) {
        super(message, cause);
    }
}