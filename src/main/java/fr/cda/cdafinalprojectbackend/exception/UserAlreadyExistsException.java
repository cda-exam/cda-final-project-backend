package fr.cda.cdafinalprojectbackend.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User already exists";

    public UserAlreadyExistsException() {
        super(ERROR_MESSAGE);
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}