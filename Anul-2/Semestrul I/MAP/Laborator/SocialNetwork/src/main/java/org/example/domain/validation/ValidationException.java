package org.example.domain.validation;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

}
