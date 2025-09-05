package org.example.zboruri.domain;

import java.time.LocalDateTime;

public class Flight extends Entity{
    private String from;
    private String to;
    private LocalDateTime departureTime;
    private LocalDateTime landingTIme;

    public int getAvaibleseats() {
        return leftSeats;
    }

    public void setAvaibleseats(int leftSeats) {
        this.leftSeats = leftSeats;
    }

    private int seats;
    private int leftSeats;


    public Flight(Long ID, String from, String to, LocalDateTime departureTime, LocalDateTime landingTIme, int seats,int leftSeats) {
        super(ID);
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.landingTIme = landingTIme;
        this.seats = seats;
        this.leftSeats = leftSeats;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDateTime getDeparturetime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getLandingtime() {
        return landingTIme;
    }

    public void setLandingTIme(LocalDateTime landingTIme) {
        this.landingTIme = landingTIme;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
