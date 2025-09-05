package org.example.restaurant.service;

import org.example.restaurant.domain.Employee;
import org.example.restaurant.repository.RepositoryEmployee;

import java.sql.SQLException;

public class ServiceEmployee {
    private RepositoryEmployee repositoryEmployee;
    public ServiceEmployee(RepositoryEmployee repositoryEmployee) {
        this.repositoryEmployee = repositoryEmployee;
    }
    public Iterable<Employee> findAll() throws SQLException {
        return repositoryEmployee.findAll();
    }
    public Employee findOne(int id) throws SQLException {
        return repositoryEmployee.findOne(id);
    }
}
