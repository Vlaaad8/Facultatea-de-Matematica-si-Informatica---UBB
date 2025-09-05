package org.example.restaurantnou.service;

import org.example.restaurantnou.domain.Order;
import org.example.restaurantnou.repository.RepositoryOrder;
import org.example.restaurantnou.repository.RepositoryTable;

public class ServiceOrder {
    private RepositoryOrder repositoryOrder;

    public ServiceOrder(RepositoryOrder repositoryOrder) {
        this.repositoryOrder = repositoryOrder;
    }

    public Iterable<Order> findAll(){
        return repositoryOrder.findAll();
    }

    public Long findLastId(){
        Long auxiliary=-1L;
        for(Order order2 : repositoryOrder.findAll()){
            if(order2.getId()>auxiliary){
                auxiliary=order2.getId();
            }
        }
        return auxiliary;
    }

    public void save(Order order){
        repositoryOrder.save(order);
    }
}
