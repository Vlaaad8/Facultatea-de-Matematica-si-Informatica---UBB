package com.example.scheletseminar9.validator;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
