package com.example.JDK;

import com.example.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;

public class ArrayRepository<T> implements InMemoryRepository<T> {
    private final List<T> array=new ArrayList<>();
    @Override
    public T add(T element) {
            array.add(element);
            return element;
    }

    @Override
    public void remove(T element) {
        array.remove(element);
    }

    @Override
    public T contains(T element) {
        if(array.contains(element)){
            return element;
        }
        return null;
    }
}
