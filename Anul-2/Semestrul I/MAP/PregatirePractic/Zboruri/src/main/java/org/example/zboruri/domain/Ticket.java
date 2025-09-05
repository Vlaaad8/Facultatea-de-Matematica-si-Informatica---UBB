package org.example.zboruri.domain;

import java.time.LocalDateTime;

public class Ticket extends Entity{
    private String username;
    private Long flightId;

    public String getUsername() {
        return username;
    }

    public Long getFlightId() {
        return flightId;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    private LocalDateTime purchaseTime;

    public Ticket(Long ID, String username, Long flightId, LocalDateTime purchaseTime) {
        super(ID);
        this.username = username;
        this.flightId = flightId;
        this.purchaseTime = purchaseTime;
    }

    public Ticket(String username, Long flightId, LocalDateTime purchaseTime) {
        this.username = username;
        this.flightId = flightId;
        this.purchaseTime = purchaseTime;
    }
}
