package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="gamesettings")
public class Configuration extends org.example.Entity<Integer> {
    @Column(nullable = false)
    private String config;


    public Configuration(){

    }

    public Configuration(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
