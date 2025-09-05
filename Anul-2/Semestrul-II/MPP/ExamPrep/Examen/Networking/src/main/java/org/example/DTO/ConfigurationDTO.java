package org.example.DTO;

import java.io.Serializable;

public class ConfigurationDTO implements Serializable {
    private int id;
    private String configuration;


    public ConfigurationDTO(int id, String configuration) {
        this.id = id;
        this.configuration = configuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
