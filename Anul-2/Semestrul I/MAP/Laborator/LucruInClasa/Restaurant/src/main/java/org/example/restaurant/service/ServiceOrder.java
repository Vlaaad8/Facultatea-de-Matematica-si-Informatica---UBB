package org.example.restaurant.service;

import org.example.restaurant.domain.Order;
import org.example.restaurant.repository.RepositoryOrder;

import java.sql.SQLException;

public class ServiceOrder {
    private RepositoryOrder repositoryOrder;


    public ServiceOrder(RepositoryOrder repositoryOrder) {
        this.repositoryOrder = repositoryOrder;
    }

    public Iterable<Order> findAll() throws SQLException {
        return repositoryOrder.findAll();
    }

    public Order Save(Long tableID){
        Order order = new Order(tableID);
        return repositoryOrder.save(order);
    }
    public int findOne(Order order) throws SQLException {
        for (Order order1:findAll()){
            if(order1.getDate().equals(order.getDate())  && order1.getTableId().equals(order.getTableId())){
                return order1.getId();
            }
        }
        return 0;
    }
}
