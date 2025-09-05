package org.example.restaurantnou.service;

import org.example.restaurantnou.domain.Staff;
import org.example.restaurantnou.repository.RepositoryStaff;

public class ServiceStaff {
    private RepositoryStaff repositoryStaff;
    public ServiceStaff(RepositoryStaff repositoryStaff) {
        this.repositoryStaff = repositoryStaff;
    }

    public Iterable<Staff> findAll(){
        return repositoryStaff.findAll();
    }
}
