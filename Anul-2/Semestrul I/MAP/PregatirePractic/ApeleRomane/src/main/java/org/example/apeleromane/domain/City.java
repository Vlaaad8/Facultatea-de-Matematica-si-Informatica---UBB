package org.example.apeleromane.domain;

public class City extends Entity {
    private String name;
    private Long river;
    private int minimumRisk;
    private int maximumRisk;

    public City(String name, Long river, int minimumRisk, int maximumRisk) {
        this.name = name;
        this.river = river;
        this.minimumRisk = minimumRisk;
        this.maximumRisk = maximumRisk;
    }

    public String getName() {
        return name;
    }

    public Long getRiver() {
        return river;
    }

    public int getMinimumrisk() {
        return minimumRisk;
    }

    public int getMaximumrisk() {
        return maximumRisk;
    }
}
