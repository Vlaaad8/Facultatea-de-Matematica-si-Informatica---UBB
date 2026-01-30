package com.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Annulment implements Serializable {
    private final int id;
    private final int orderId;
    private final LocalDateTime timestamp;
    private final String reason;

    public Annulment(int id, int orderId, String reason) {
        this.id = id;
        this.orderId = orderId;
        this.timestamp = LocalDateTime.now();
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getReason() {
        return reason;
    }


}
