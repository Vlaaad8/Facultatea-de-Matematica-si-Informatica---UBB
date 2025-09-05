package org.example.restaurantnou.repository;

import org.example.restaurantnou.domain.Staff;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryStaff extends AbstractDBRepository{

    public RepositoryStaff(String username, String password, String url) {
        super(username, password, url);
    }

    public Iterable<Staff> findAll(){
        Set<Staff> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM staff");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Staff staff=new Staff(name);
                staff.setId(id);
                tables.add(staff);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }
}
