package org.example.restaurant.domain;

import java.sql.Date;
import java.time.LocalDateTime;

public class Order {
    private int id;
    private Long tableId;
    private LocalDateTime date;
    private OrderStatus status;

    public Order(Long tableId){
        this.tableId = tableId;
        this.date = LocalDateTime.now();
        status=OrderStatus.PLACED;
    }

    public int getId() {
        return id;
    }

    public Long getTableId() {
        return tableId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Order(int id, Long tableId, LocalDateTime date, OrderStatus status){
        this.id = id;
        this.tableId = tableId;
        this.date = date;
        this.status = status;

    }
}
