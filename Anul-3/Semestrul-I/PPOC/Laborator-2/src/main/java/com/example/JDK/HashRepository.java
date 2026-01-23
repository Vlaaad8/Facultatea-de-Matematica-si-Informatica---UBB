package com.example.JDK;

import com.example.InMemoryRepository;

import java.util.HashSet;
import java.util.Set;

public class HashRepository<T> implements InMemoryRepository<T> {
    private final Set<T> set= new HashSet<>();
    @Override
    public T add(T element) {
        set.add(element);
        return element;
    }

    @Override
    public void remove(T element) {
        set.remove(element);
    }

    @Override
    public T contains(T element) {
        if(set.contains(element)){
            return element;
        }
        return null;
    }
}
