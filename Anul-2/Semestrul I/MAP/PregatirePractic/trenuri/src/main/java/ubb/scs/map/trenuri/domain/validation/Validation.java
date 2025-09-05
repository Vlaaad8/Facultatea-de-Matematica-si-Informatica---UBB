package ubb.scs.map.trenuri.domain.validation;

public interface Validation<T>{
    void validate(T entity) throws ValidationException;
}

