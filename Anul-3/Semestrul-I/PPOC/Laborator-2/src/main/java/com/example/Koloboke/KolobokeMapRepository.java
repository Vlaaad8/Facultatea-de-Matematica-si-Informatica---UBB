package com.example.Koloboke;

import com.example.InMemoryRepository;
import com.koloboke.collect.map.ObjObjMap;
import com.koloboke.collect.map.hash.HashObjObjMaps;

public class KolobokeMapRepository<T> implements InMemoryRepository<T> {
    private final ObjObjMap<T,T> map = HashObjObjMaps.newMutableMap();
    @Override
    public T add(T element) {
        map.put(element, element);
        return element;
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
