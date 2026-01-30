package com.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private final int id;
    private final int clientId;
    private final Instrument instrument;
    private final OrderType type;
    private final double quantity;
    private final double price;
    private OrderStatus status;
    private final LocalDateTime timestamp;

    public Order(int id, int clientId, Instrument instrument, OrderType type, double quantity, double price) {
        this.id = id;
        this.clientId = clientId;
        this.instrument = instrument;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.status = OrderStatus.PENDING;
        this.timestamp = LocalDateTime.now();
    }


    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public OrderType getType() {
        return type;
    }


    public double getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }
    public double getQuantity() {
        return quantity;
    }
}
