package org.example.demo.domain;

public class TrainStation extends Entity{
    private String departureCity;
    private String arrivalCity;

    public TrainStation(String id,String departureCity, String arrivalCity) {
        super(id);
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }
}
