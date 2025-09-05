package org.example.restaurant.service;

import org.example.restaurant.domain.MenuItem;
import org.example.restaurant.domain.OrderItem;
import org.example.restaurant.repository.RepositoryMenuItem;
import org.example.restaurant.repository.RepositoryOrderItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceOrderItem {
    private RepositoryOrderItem repositoryOrderItem;
    private RepositoryMenuItem repositoryMenuItem;
    public ServiceOrderItem(RepositoryOrderItem repositoryOrderItem,RepositoryMenuItem repositoryMenuItem) {
        this.repositoryOrderItem = repositoryOrderItem;
        this.repositoryMenuItem = repositoryMenuItem;
    }
    public Iterable<OrderItem> findAll() throws SQLException {
        return repositoryOrderItem.findAll();
    }
    public void Save(int orderID,Long menuItemID) throws SQLException {
        OrderItem order = new OrderItem(orderID,menuItemID);
        repositoryOrderItem.save(order);
    }

    public ArrayList<String> foodList(int orderID) {
        for(OrderItem orderItem:findAll()){
            if(orderItem.getOrderID()==orderID){
                MenuItem menuItem = repositoryMenuItem.findOne(orderItem.getMenuItemID());
            }
        }
    }
}
