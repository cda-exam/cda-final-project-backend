package fr.cda.cdafinalprojectbackend.exception;

public class UserNotFoundException extends Exception {
    private static final String ERROR_MESSAGE = "User not found";

    public UserNotFoundException() {
        super(ERROR_MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
