package org.example.restaurantnou.domain;

import java.time.LocalDateTime;

public class OrderMenuDTO {
    private Long tableID;
    private LocalDateTime orderDate;
    private String items;

    public OrderMenuDTO(Long tableID, LocalDateTime orderDate, String items) {
        this.tableID = tableID;
        this.orderDate = orderDate;
        this.items = items;
    }

    public Long getTableID() {
        return tableID;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getItems() {
        return items;
    }
}
