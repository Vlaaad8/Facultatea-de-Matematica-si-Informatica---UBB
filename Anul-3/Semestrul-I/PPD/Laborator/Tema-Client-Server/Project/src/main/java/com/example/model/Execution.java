package com.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Execution implements Serializable {
    private final int id;
    private final int orderId;
    private final LocalDateTime timestamp;
    private final double executedVolume;
    private final double finalPrice;
    private final double commission;

    public Execution(int id, int orderId, double executedVolume, double finalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.timestamp = LocalDateTime.now();
        this.executedVolume = executedVolume;
        this.finalPrice = finalPrice;
        this.commission = finalPrice * executedVolume * 0.005;
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

    public double getExecutedVolume() {
        return executedVolume;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getCommission() {
        return commission;
    }
}
