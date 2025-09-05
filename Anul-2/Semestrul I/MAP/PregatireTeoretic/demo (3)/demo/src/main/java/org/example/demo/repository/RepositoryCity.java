package org.example.demo.repository;

import org.example.demo.domain.City;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryCity extends AbstractDBRepository{


    public RepositoryCity(String username, String password, String url) {
        super(username, password, url);
    }

    public Iterable<City> findAll() throws SQLException {
        Set<City> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM city2");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name= resultSet.getString("name");
;
                City city=new City(id,name);
                tables.add(city);
            }
        }
        return tables;
    }
}


