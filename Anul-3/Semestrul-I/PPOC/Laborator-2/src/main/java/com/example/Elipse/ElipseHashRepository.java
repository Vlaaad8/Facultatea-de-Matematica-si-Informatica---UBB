package com.example.Elipse;

import com.example.InMemoryRepository;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;

public class ElipseHashRepository<T> implements InMemoryRepository<T> {

    private final MutableSet<T> set = UnifiedSet.newSet();
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
