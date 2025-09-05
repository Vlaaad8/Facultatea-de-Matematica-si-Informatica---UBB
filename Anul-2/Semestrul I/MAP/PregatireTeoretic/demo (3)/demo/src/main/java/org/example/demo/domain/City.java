package org.example.demo.domain;

public class City extends Entity{
    private String cityName;

    public City(String id,String cityName) {
        super(id);
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
