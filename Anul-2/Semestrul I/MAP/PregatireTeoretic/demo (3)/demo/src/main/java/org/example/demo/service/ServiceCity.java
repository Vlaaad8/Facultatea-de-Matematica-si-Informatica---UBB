package org.example.demo.service;

import org.example.demo.domain.City;
import org.example.demo.repository.RepositoryCity;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ServiceCity {
    private RepositoryCity repositoryCity;

    public ServiceCity(RepositoryCity repositoryCity) {
        this.repositoryCity = repositoryCity;

    }

    public Iterable<City> findAll() {
        try {
            return repositoryCity.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> destinations() {
        Set<String> destination=new HashSet<>();
        for(City city : findAll()) {
            destination.add(city.getCityName());
        }
        return destination;
    }
}