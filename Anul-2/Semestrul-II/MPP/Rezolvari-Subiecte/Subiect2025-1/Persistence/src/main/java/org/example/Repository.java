package org.example;

import java.util.Collection;
import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {


    Optional<E> add(E entity);

    Optional<E> delete(E entity);

    Optional<E> findById(ID id);

    Iterable<E> findAll();

}



