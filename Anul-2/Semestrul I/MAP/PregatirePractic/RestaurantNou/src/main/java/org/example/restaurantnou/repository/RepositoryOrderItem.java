package org.example.restaurantnou.repository;

import org.example.restaurantnou.domain.Order;
import org.example.restaurantnou.domain.OrderItem;
import org.example.restaurantnou.domain.OrderStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class RepositoryOrderItem extends AbstractDBRepository{
    public RepositoryOrderItem(String username, String password, String url) {
        super(username, password, url);
    }

    public Iterable<OrderItem> findAll(){
        Set<OrderItem> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM orderitem");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long menuitemid = resultSet.getLong("menuitemid");
                Long orderid = resultSet.getLong("orderid");
                OrderItem orderItem = new OrderItem(orderid, menuitemid);
                tables.add(orderItem);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }
    public OrderItem save(OrderItem entity)  {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"orderitem\" (orderid,menuitemid) VALUES (?, ?)")){
            statement.setLong(1, entity.getOrderid());
            statement.setLong(2, entity.getMenuitemid());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
        if (rez > 0)
            return null;
        else
            return entity;
    }
}
