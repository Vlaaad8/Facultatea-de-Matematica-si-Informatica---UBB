package org.example.clientfx;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name="tickets")
public class Ticket extends Entity<Integer> {
    @Column(nullable = false)
    private String buyer;
    @ManyToOne
    //@JoinColumn(name="id",nullable=false)
    private Flight flight;
    @Column(nullable = false)
    private int numberOfTickets;

    public Ticket(String buyer, Flight flight, int numberOfTickets) {
        this.buyer = buyer;
        this.flight = flight;
        this.numberOfTickets = numberOfTickets;
    }
    public Ticket() {

    }
    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "buyer='" + buyer + '\'' +
                ", flight=" + flight +
                ", numberOfTickets=" + numberOfTickets +
                '}';
    }
}
