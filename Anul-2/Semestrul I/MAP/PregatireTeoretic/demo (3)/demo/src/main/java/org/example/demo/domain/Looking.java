package org.example.demo.domain;

public class Looking {
    private Long id;
    private String departure;
    private String destination;

    public Looking(String departure, String destination) {
        this.departure = departure;
        this.destination = destination;
    }

    public Long getId() {
        return id;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
