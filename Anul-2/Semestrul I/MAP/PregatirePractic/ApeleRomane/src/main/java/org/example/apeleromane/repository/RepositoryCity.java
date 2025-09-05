package org.example.apeleromane.repository;

import org.example.apeleromane.domain.*;
import org.example.apeleromane.domain.River;

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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM city");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name= resultSet.getString("name");
                Long river = resultSet.getLong("river");
                int minimumRisk= resultSet.getInt("minimumrisk");
                int maximumRisk= resultSet.getInt("maximumrisk");
                City city=new City(name, river, minimumRisk, maximumRisk);
                city.setId(id);
                tables.add(city);
            }
        }
        return tables;
    }
}
