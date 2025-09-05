package org.example.sem13.repository;

import org.example.sem13.domain.Entity;

import java.util.Enumeration;

public interface Repository<ID, T extends Entity<ID>> {
    Enumeration<T> findAll();
}