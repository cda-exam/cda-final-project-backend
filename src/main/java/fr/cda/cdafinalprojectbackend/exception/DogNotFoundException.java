package fr.cda.cdafinalprojectbackend.exception;

public class DogNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Dog not found";

    public DogNotFoundException() {
        super(ERROR_MESSAGE);
    }

    public DogNotFoundException(String message) {
        super(message);
    }

    public DogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
