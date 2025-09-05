package org.example.restaurant.repository;

import javafx.scene.control.Menu;
import org.example.restaurant.domain.Employee;
import org.example.restaurant.domain.MenuItem;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryMenuItem extends AbstractDBRepository<MenuItem> {

    public RepositoryMenuItem(String username, String password, String url) {
        super(username, password, url);
    }

    @Override
    public Iterable<MenuItem> findAll() throws SQLException {
        Set<MenuItem> items = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"menuitem\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String category=resultSet.getString("category");
                String item = resultSet.getString("item");
                float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");
                items.add(new MenuItem(id,category,item,price,currency));
            }
        }
        return items;
    }
    @Override
    public MenuItem findOne(int id) throws SQLException {
        return null;
    }
}
