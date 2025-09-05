package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="players")
public class Player extends org.example.Entity<Integer> {
    @Column(nullable = false,unique = true)
    private String name;

    public Player(String name) {
        this.name = name;
    }
    public Player() {

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
