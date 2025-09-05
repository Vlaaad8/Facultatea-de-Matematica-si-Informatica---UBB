package org.example.restaurantnou.service;

import org.example.restaurantnou.domain.Table;
import org.example.restaurantnou.repository.RepositoryTable;

public class ServiceTable {
    private RepositoryTable repositoryTable;

    public ServiceTable(RepositoryTable repositoryTable) {
        this.repositoryTable = repositoryTable;

    }
    public Iterable<Table> findAll(){
        return repositoryTable.findAll();
    }
}
