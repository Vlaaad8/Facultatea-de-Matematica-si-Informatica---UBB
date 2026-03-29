package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="configurations")
public class Configuration extends org.example.Entity<Integer> {
    @Column(nullable = false)
    String configuration;

    public Configuration(){

    }

    public Configuration(String configuration) {
        this.configuration = configuration;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
