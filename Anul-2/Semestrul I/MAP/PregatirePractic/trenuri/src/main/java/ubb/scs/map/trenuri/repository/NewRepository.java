package ubb.scs.map.trenuri.repository;

import ubb.scs.map.trenuri.domain.Entity;
import ubb.scs.map.trenuri.domain.validation.ValidationException;

import java.util.Optional;

/**
 * CRUD operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> - type of entities saved in repository
 */
public interface NewRepository<ID, E extends Entity<ID>>{
    /**
     *
     * @param id -the id of the entity to be returned
     * id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException
     * if id is null.
     */
    Optional<E> findOne(ID id);
    /**
     *
     * @return all entities
     */
    Iterable<E> findAll();
    /**
     *
     * @param entity
     * entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     * - the entity (id already exists)
     * @throws ValidationException
     * if the entity is not valid
     * @throws IllegalArgumentException
     * if the given entity is null. *
     */
}
