package org.example.restaurantnou.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order extends Entity{
    private Long tableID;
    private LocalDateTime orderDate;
    private OrderStatus status;

    public Order(Long tableID, LocalDateTime orderDate, OrderStatus status) {
        this.tableID = tableID;
        this.orderDate = orderDate;
        this.status = status;
    }



    public Long getTableid() {
        return tableID;
    }

    public LocalDateTime getOrderdate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
