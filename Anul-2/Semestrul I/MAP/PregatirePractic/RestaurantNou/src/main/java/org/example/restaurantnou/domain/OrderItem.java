package org.example.restaurantnou.domain;

public class OrderItem {
    private Long orderID;
    private Long menuItemID;

    public OrderItem(Long orderID, Long menuItemID) {
        this.orderID = orderID;
        this.menuItemID = menuItemID;
    }

    public Long getOrderid() {
        return orderID;
    }

    public Long getMenuitemid() {
        return menuItemID;
    }
}
