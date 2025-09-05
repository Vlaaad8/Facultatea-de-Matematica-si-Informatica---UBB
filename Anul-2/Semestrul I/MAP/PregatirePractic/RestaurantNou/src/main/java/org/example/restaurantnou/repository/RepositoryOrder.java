package org.example.restaurantnou.repository;

import org.example.restaurantnou.domain.MenuItem;
import org.example.restaurantnou.domain.Order;
import org.example.restaurantnou.domain.OrderStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class RepositoryOrder extends AbstractDBRepository{
    public RepositoryOrder(String username, String password, String url) {
        super(username, password, url);
    }

    public Iterable<Order> findAll(){
        Set<Order> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"order\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long tableid = resultSet.getLong("tableid");
                LocalDateTime date = resultSet.getTimestamp("dateorder").toLocalDateTime();
                OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));
                Order order=new Order(tableid,date,status);
                order.setId(id);
                tables.add(order);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    public Order save(Order entity)  {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"order\" (tableid,dateorder,status) VALUES (?, ?, ?)")){
            statement.setLong(1, entity.getTableid());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getOrderdate()));
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

    public Order findOne(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"order\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long tableid = resultSet.getLong("tableid");
                LocalDateTime date = resultSet.getTimestamp("dateorder").toLocalDateTime();
                OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));
                Order order=new Order(tableid,date,status);
                order.setId(id);
                return order;
            }
        }
        return null;
    }
}
