package org.example.apeleromane.service;

import org.example.apeleromane.domain.City;
import org.example.apeleromane.domain.River;
import org.example.apeleromane.repository.RepositoryCity;
import org.example.apeleromane.repository.RepositoryRiver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCity {
    private RepositoryRiver repositoryRiver;
    private RepositoryCity repositoryCity;

    public ServiceCity(RepositoryRiver repositoryRiver, RepositoryCity repositoryCity) {
        this.repositoryRiver = repositoryRiver;
        this.repositoryCity = repositoryCity;

    }

    public Iterable<City> findAll(){
        try {
            return repositoryCity.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<City>> sortByRisk(){
        Map<String, List<City>> map = new HashMap<>();
        List<City> lowRisk= new ArrayList<>();
        List<City> highRisk= new ArrayList<>();
        List<City> mediumRisk= new ArrayList<>();

        for(City city : findAll()){
            try {
                River river = repositoryRiver.findOne(city.getRiver());
                if (river.getCapacity() < city.getMinimumrisk()) {
                    lowRisk.add(city);
                } else if (river.getCapacity() < city.getMaximumrisk()) {
                    mediumRisk.add(city);
                } else {
                    highRisk.add(city);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        map.put("lowRisk", lowRisk);
        map.put("highRisk", highRisk);
        map.put("mediumRisk", mediumRisk);
        return map;
    }
}
