package org.example.restaurant.service;

import org.example.restaurant.domain.Table;
import org.example.restaurant.repository.RepositoryTable;

import java.sql.SQLException;

public class ServiceTable {
    RepositoryTable table;
    public ServiceTable(RepositoryTable table) {
        this.table = table;
    }

    public Iterable<Table> findAll() throws SQLException {
        return table.findAll();
    }

    public Table findById(int id) throws SQLException {
        return table.findOne(id);
    }
}
