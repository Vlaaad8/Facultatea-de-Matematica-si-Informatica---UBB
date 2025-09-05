package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="gamesettings")
public class Configuration extends org.example.Entity<Integer> {
    @Column(nullable = false)
    private int i;
    @Column(nullable = false)
    private int j;
    @Column(nullable = false)
    private String text;

    public Configuration(int iCoordinate, int jCoordinate, String text) {
        this.i = iCoordinate;
        this.j = jCoordinate;
        this.text = text;
    }

    public Configuration(){

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
