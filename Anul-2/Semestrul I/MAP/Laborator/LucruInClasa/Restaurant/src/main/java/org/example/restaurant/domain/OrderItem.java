package org.example.restaurant.domain;

public class OrderItem {
    private int orderID;
    private Long menuItemID;


    public OrderItem(int orderID, Long menuItemID) {
        this.orderID = orderID;
        this.menuItemID = menuItemID;
    }

    public int getOrderID() {
        return orderID;
    }

    public Long getMenuItemID() {
        return menuItemID;
    }
}
