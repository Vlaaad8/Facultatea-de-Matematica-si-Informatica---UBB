package org.example.iss.service;


import lombok.AllArgsConstructor;
import org.example.iss.domain.*;
import org.example.iss.repository.*;

import java.util.List;

@AllArgsConstructor
public class Service {
    private UserRepository userRepository;
    private DrugRepository drugRepository;
    private SpecialOrderRepository specialOrderRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;

    public User login(String username, String password) {
        User user = new User(username, password, "", "", Role.None);
        return userRepository.login(user);
    }

    public User register(String username, String password, String firstname, String lastname, Role role) {
        User user = new User(username, password, firstname, lastname, role);
        return userRepository.save(user);
    }

    public Drug add(String name, String type, float price, String observations, int availableUnits) {
        Drug drug = new Drug(name, type, price, observations, availableUnits);
        return drugRepository.save(drug);
    }

    public Drug delete(Drug drug) {
        return drugRepository.delete(drug);
    }

    public Drug update(int id,String name, String type, float price, String observations, int availableUnits) {
        Drug drug = new Drug(name, type, price, observations, availableUnits);
        drug.setId(id);
        return drugRepository.update(drug);
    }
    public List<Drug> findAll(){
        return drugRepository.findAll();
    }

    public void saveSpecialOrder(String description){
        SpecialOrder specialOrder = new SpecialOrder(description);
        specialOrderRepository.save(specialOrder);
    }

    public int saveOrder(User user,int quantity){
        Order oder = new Order(user,quantity);
        return orderRepository.save(oder);
    }
    public void saveOrderItem(Drug drug,Order o){
        OrderItem orderItem=new OrderItem(o,drug);
        orderItemRepository.save(orderItem);

    }
    public List<Order> findAllOrders(){
        return orderRepository.findAll();
    }
    public List<Drug> findDrugByOrder(Order order){
        return orderItemRepository.getDrugsByOrder(order);
    }
    public void update(Order order){
        orderRepository.update(order);
    }
}
