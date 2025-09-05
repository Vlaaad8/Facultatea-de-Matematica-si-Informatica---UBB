package org.example.restaurantnou.repository;

import org.example.restaurantnou.domain.MenuItem;
import org.example.restaurantnou.domain.Staff;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryMenuItem extends AbstractDBRepository{
    public RepositoryMenuItem(String username, String password, String url) {
        super(username, password, url);
    }

    public Iterable<MenuItem> findAll(){
        Set<MenuItem> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM menuitem");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String category= resultSet.getString("category");
                String item = resultSet.getString("item");
                float price = resultSet.getFloat("price");
                String currency= resultSet.getString("currency");
                MenuItem menuItem=new MenuItem(category,item,price,currency);
                menuItem.setId(id);
                tables.add(menuItem);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    public MenuItem findOne(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"menuitem\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String category = resultSet.getString("category");
                String item = resultSet.getString("item");
                float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");
                MenuItem menuItem=new MenuItem(category,item,price,currency);
                menuItem.setId(id);
                return menuItem;
            }
        }
        return null;
    }
}
