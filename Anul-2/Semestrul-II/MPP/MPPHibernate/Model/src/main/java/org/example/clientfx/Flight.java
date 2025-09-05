package org.example.clientfx;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name="flights")
public class Flight extends Entity<Integer> {
    @Column(nullable = false)
    private String origin;
    @Column(nullable = false)
    private String departure;
    @Column(nullable = false)
    private int availableSeats;
    @Column(nullable = false)
    private String airport;
    @Column(nullable = false,columnDefinition ="TIMESTAMP")
    private LocalDateTime daytime;

    public Flight(String origin, String departure, int availableSeats, String airport, LocalDateTime daytime) {
        this.origin = origin;
        this.departure = departure;
        this.availableSeats = availableSeats;
        this.airport = airport;
        this.daytime = daytime;
    }
    public Flight(){

    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public LocalDateTime getDaytime() {
        return daytime;
    }

    public void setdayTime(LocalDateTime daytime) {
        this.daytime = daytime;
    }

    @Override
    public String toString() {
        return "Flight:" + origin + ' ' +
                 departure +  ' ' +
                 availableSeats + ' '+
                 airport + ' ' +
                 daytime + '\'';
    }
}
