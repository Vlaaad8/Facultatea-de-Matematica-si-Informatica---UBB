package org.example.restaurantnou.service;

import org.example.restaurantnou.domain.MenuItem;
import org.example.restaurantnou.domain.Order;
import org.example.restaurantnou.domain.OrderItem;
import org.example.restaurantnou.domain.OrderMenuDTO;
import org.example.restaurantnou.domain.event.ChangeEventType;
import org.example.restaurantnou.domain.event.OrderEntityChange;
import org.example.restaurantnou.domain.observer.Observable;
import org.example.restaurantnou.domain.observer.Observer;
import org.example.restaurantnou.repository.RepositoryMenuItem;
import org.example.restaurantnou.repository.RepositoryOrder;
import org.example.restaurantnou.repository.RepositoryOrderItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceOrderItem implements Observable<OrderEntityChange> {
    private RepositoryOrderItem repositoryOrderItem;
    private RepositoryMenuItem repositoryMenuItem;
    private RepositoryOrder repositoryOrder;

    private List<Observer<OrderEntityChange>> observers = new ArrayList<>();
    public ServiceOrderItem(RepositoryOrderItem repositoryOrderItem,RepositoryMenuItem repositoryMenuItem,RepositoryOrder repositoryOrder) {
        this.repositoryOrderItem = repositoryOrderItem;
        this.repositoryMenuItem = repositoryMenuItem;
        this.repositoryOrder = repositoryOrder;
    }

    public Iterable<OrderItem> findAll(){
        return repositoryOrderItem.findAll();
    }


    public String foodList(Long orderID) throws SQLException {
        String foodList="";
        for(OrderItem orderItem : repositoryOrderItem.findAll()){
           if(orderItem.getOrderid().equals(orderID)) {
               MenuItem menuItem=repositoryMenuItem.findOne(orderItem.getMenuitemid());
               foodList=foodList+menuItem.getItem()+" ";
           }
        }
        return foodList;
    }
    public void save(OrderItem orderItem){
        OrderItem a=repositoryOrderItem.save(orderItem);
        OrderEntityChange event = new OrderEntityChange(ChangeEventType.ADD, a);
        notifyObservers(event);
    }

    public Set<Long> individualOrders(){
        Set<Long> individualOrders=new HashSet<Long>();
        for(OrderItem orderItem : repositoryOrderItem.findAll()){
            individualOrders.add(orderItem.getOrderid());
        }
        return individualOrders;
    }

    public List<OrderMenuDTO> showStaffOrder() throws SQLException {
        Set<Long> individualOrders = individualOrders();
        List<OrderMenuDTO> all = new ArrayList<>();
        for (Long orderID : individualOrders) {
            Order order = repositoryOrder.findOne(orderID);
            OrderMenuDTO orderMenuDTO = new OrderMenuDTO(order.getTableid(), order.getOrderdate(), foodList(orderID));
            all.add(orderMenuDTO);
        }

        return all;
    }

    @Override
    public void addObserver(Observer<OrderEntityChange> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<OrderEntityChange> e) {

    }

    @Override
    public void notifyObservers(OrderEntityChange t) {
        observers.stream().forEach(x -> x.update(t));
    }
}

