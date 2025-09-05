package org.example.demo.repository;

import org.example.demo.domain.City;
import org.example.demo.domain.TrainStation;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryTrainStation extends AbstractDBRepository {

    public RepositoryTrainStation(String username, String password, String url) {
        super(username, password, url);
    }

    public Iterable<TrainStation> findAll() throws SQLException {
        Set<TrainStation> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM trainstation");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String departurecity = resultSet.getString("departurecity");
                String destinationcity = resultSet.getString("destinationcity");
                TrainStation city = new TrainStation(id,departurecity, destinationcity);
                tables.add(city);
            }
        }
        return tables;
    }
}
