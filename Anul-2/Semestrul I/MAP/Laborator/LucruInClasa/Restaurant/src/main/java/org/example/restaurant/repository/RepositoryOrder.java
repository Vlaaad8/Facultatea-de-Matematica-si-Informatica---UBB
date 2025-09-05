package org.example.restaurant.repository;

import org.example.restaurant.domain.MenuItem;
import org.example.restaurant.domain.Order;
import org.example.restaurant.domain.OrderStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepositoryOrder extends AbstractDBRepository<Order> {
    public RepositoryOrder(String username, String password, String url) {
        super(username, password, url);
    }

    public Order save(Order entity)  {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"order\" (tableid,date,status) VALUES (?, ?, ?)")){
            statement.setLong(1, entity.getTableId());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setString(3, entity.getStatus().toString());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
        if (rez > 0)
            return entity;
        else
            return null;
    }

    @Override
    public Iterable<Order> findAll() throws SQLException {
        Set<Order> items = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"order\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Long idTable=resultSet.getLong("tableid");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
               OrderStatus status =OrderStatus.valueOf(resultSet.getString("status"));
                items.add(new Order(id,idTable,date,status));
            }
        }
        return items;
    }

    @Override
    public Order findOne(int id) throws SQLException {
        return null;
    }
}
