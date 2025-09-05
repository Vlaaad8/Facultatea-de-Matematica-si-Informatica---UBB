package org.example.apeleromane.repository;

import org.example.apeleromane.domain.River;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryRiver extends AbstractDBRepository{

    public RepositoryRiver(String username, String password, String url) {
        super(username, password, url);
    }

    public Iterable<River> findAll() throws SQLException {
        Set<River> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM river");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name= resultSet.getString("name");
                int capacity= resultSet.getInt("capacity");
                River river=new River(name,capacity);
                river.setId(id);
                tables.add(river);
            }
        }
        return tables;
    }

    public River findOne(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"river\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int capacity = resultSet.getInt("capacity");
                River river=new River(name,capacity);
                river.setId(id);
                return river;
            }
        }
        return null;
    }

    public River update(River entity) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(),getPassword())) {
            PreparedStatement statement = connection.prepareStatement("UPDATE river SET capacity=? WHERE id = ?");
            statement.setInt(1, entity.getCapacity());
            statement.setLong(2, entity.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return entity;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }
}
