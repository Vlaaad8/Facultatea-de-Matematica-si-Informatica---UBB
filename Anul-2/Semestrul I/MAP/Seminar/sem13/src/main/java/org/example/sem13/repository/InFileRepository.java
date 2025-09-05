package org.example.sem13.repository;

import org.example.sem13.domain.Entity;

public abstract class InFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> implements Repository<ID, E> {
    protected String fileName;

    abstract void loadFromFile();

    public InFileRepository(String fileName) {
        super();
        this.fileName = fileName;
    }
}
