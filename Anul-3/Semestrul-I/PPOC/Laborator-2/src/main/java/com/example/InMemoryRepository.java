package com.example;

public interface InMemoryRepository<T> {

    T add(T element);
    void remove(T element);
    T contains(T element);


}
