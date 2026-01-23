package com.example.Elipse;

import com.example.InMemoryRepository;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;

public class ElipseArrayRepository<T> implements InMemoryRepository<T> {
    private final MutableList<T> list= FastList.newList();
    @Override
    public T add(T element) {
        list.add(element);
        return element;
    }

    @Override
    public void remove(T element) {
        list.remove(element);
    }

    @Override
    public T contains(T element) {
        if(list.contains(element)){
            return element;
        }
        return null;
    }
}
