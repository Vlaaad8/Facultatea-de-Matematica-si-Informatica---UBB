package org.example.restaurant.repository;

import org.example.restaurant.domain.Table;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryTable extends AbstractDBRepository<Table>{
    public RepositoryTable(String username, String password,String url) {
        super(username, password,url);
    }

    @Override
    public Iterable<Table> findAll() throws SQLException {
        Set<Table> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"tables\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                tables.add(new Table(id));
            }
        }
        return tables;
    }

    @Override
    public Table findOne(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"tables\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                return new Table(id1);
            }
        }
        return null;
    }
}
