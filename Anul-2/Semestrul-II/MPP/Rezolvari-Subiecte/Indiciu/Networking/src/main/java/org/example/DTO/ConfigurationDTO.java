package org.example.DTO;

import java.io.Serializable;

public class ConfigurationDTO implements Serializable {
    private int id;
    private int i;
    private int j;
    private String text;

    public ConfigurationDTO(int id, int i, int j, String text) {
        this.id = id;
        this.i = i;
        this.j = j;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
