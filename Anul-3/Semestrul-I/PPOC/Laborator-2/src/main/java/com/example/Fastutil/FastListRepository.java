package com.example.Fastutil;

import com.example.InMemoryRepository;
import it.unimi.dsi.fastutil.BigList;
import it.unimi.dsi.fastutil.ints.IntBigArrayBigList;
import it.unimi.dsi.fastutil.objects.ObjectBigArrayBigList;

public class FastListRepository implements InMemoryRepository<Integer> {
    private final IntBigArrayBigList list = new IntBigArrayBigList();

    @Override
    public Integer add(Integer element) {
        list.add(element);
        return element;
    }

    @Override
    public void remove(Integer element) {
        list.rem(element);
    }

    @Override
    public Integer contains(Integer element) {
        if (list.contains(element)) {
            return element;
        }
        return null;
    }
}
