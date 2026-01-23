package com.example.Koloboke;

import com.example.InMemoryRepository;
import com.koloboke.collect.set.IntSet;
import com.koloboke.collect.set.ObjSet;
import com.koloboke.collect.set.hash.HashIntSets;
import com.koloboke.collect.set.hash.HashObjSets;


public class KolobokeSetRepository<Integer> implements InMemoryRepository<Integer> {
    private final IntSet set = HashIntSets.newMutableSet();
    @Override
    public Integer add(Integer element) {
        set.add((java.lang.Integer) element);
        return element;
    }

    @Override
    public void remove(Integer element) {
set.remove(element);
    }

    @Override
    public Integer contains(Integer element) {
        if(set.contains(element)){
            return element;
        }
        return null;
    }
}
