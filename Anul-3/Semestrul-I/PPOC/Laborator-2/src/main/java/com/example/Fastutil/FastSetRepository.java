package com.example.Fastutil;

import com.example.InMemoryRepository;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public class FastSetRepository<T> implements InMemoryRepository<T> {

    private final ObjectOpenHashSet<T> set = new ObjectOpenHashSet<>();
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
        if(set.contains(element)) {
            return element;
        }
        return null;
    }
}
