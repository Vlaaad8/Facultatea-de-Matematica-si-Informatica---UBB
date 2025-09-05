package org.example.restaurantnou.domain;

public class Staff extends Entity{
    private String name;

    public Staff(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
