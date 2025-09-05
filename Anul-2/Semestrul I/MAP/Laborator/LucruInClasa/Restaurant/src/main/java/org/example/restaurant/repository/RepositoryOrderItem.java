package org.example.restaurant.repository;

import org.example.restaurant.domain.Order;
import org.example.restaurant.domain.OrderItem;
import org.example.restaurant.domain.OrderStatus;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryOrderItem extends AbstractDBRepository<OrderItem> {
    public RepositoryOrderItem(String username, String password, String url) {
        super(username, password, url);
    }

    @Override
    public Iterable<OrderItem> findAll() throws SQLException {
        Set<OrderItem> items = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"orderstatus\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idOder = resultSet.getInt("orderid");
                Long idTable=resultSet.getLong("menuitemid");
                items.add(new OrderItem(idOder,idTable));
            }
        }
        return items;
    }

    @Override
    public OrderItem findOne(Long id) throws SQLException {
        return null;
    }

    public OrderItem save(OrderItem entity)  {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"orderstatus\" (orderid,menuitemid) VALUES (?, ?)")){
            statement.setInt(1, entity.getOrderID());
            statement.setLong(2, entity.getMenuItemID());
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
