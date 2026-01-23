package com.example.JDK;

import com.example.InMemoryRepository;

import java.util.Set;
import java.util.TreeSet;

public class TreeRepository<T> implements InMemoryRepository<T> {
    private final Set<T> treeSet = new TreeSet<>();
    @Override
    public T add(T element) {
        treeSet.add(element);
        return element;
    }

    @Override
    public void remove(T element) {
        treeSet.remove(element);
    }

    @Override
    public T contains(T element) {
        if(treeSet.contains(element)){
            return element;
        }
        return null;
    }


}
