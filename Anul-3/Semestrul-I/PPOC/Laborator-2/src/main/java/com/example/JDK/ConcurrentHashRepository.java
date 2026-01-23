package com.example.JDK;

import com.example.InMemoryRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashRepository<T> implements InMemoryRepository<T> {

    private final Map<T, T> map=new ConcurrentHashMap<>();
    @Override
    public T add(T element) {
        return map.put(element, element);
    }

    @Override
    public void remove(T element) {
        map.remove(element);
    }

    @Override
    public T contains(T element) {
        if(map.containsKey(element)){
            return map.get(element);
        }
        return null;
    }
}
