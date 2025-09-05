package org.example.demo.repository;

import org.example.demo.domain.City;
import org.example.demo.domain.Looking;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepositoryLooking extends AbstractDBRepository {
    public RepositoryLooking(String username, String password, String url) {
        super(username, password, url);
    }

    public Looking save(Looking entity)  {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"looking\" (departure,destination) VALUES (?, ?)")){
            statement.setString(1, entity.getDeparture());
            statement.setString(2, entity.getDestination());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
        if (rez > 0)
            return null;
        else
            return entity;
    }
    public Looking delete(Looking id) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"looking\" WHERE id = ?");
            statement.setLong(1, id.getId());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Iterable<Looking> findAll() throws SQLException {
        Set<Looking> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM looking");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String destination = resultSet.getString("destination");
                String departure = resultSet.getString("departure");
                Looking looking=new Looking(departure,destination);
                looking.setId(id);
                tables.add(looking);
            }
        }
        return tables;
    }

    public Looking findOne(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"looking\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String destination = resultSet.getString("destination");
                String departure = resultSet.getString("departure");
                Looking looking=new Looking(departure,destination);
                looking.setId(id);
                return looking;
            }
        }
        return null;
    }
}
