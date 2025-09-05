package org.example.zboruri.repository;

import org.example.zboruri.domain.Client;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryClient extends AbstractDBRepository<Client>{
    public RepositoryClient(String username, String password, String url) {
        super(username, password, url);
    }

    @Override
    public Iterable<Client> findAll() throws SQLException {
        Set<Client> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM client");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id=resultSet.getLong("id");
                String username = resultSet.getString("username");
                String name=resultSet.getString("name");
                Client client=new Client(id, username, name);
                tables.add(client);
            }
        }
        return tables;
    }

    @Override
    public Client findOne(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"client\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                String username= resultSet.getString("usernamename");
                String name=resultSet.getString("name");
                return new Client(id1, username, name);
            }
        }
        return null;
    }
}
