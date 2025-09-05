package org.example.domain.validation;

public interface Validation<T>{
    void validate(T entity) throws ValidationException;
    }

