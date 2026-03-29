package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name="games")
public class Game extends org.example.Entity<Integer> {
    @ManyToOne
    private Player player;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private int tries;
    @Column(nullable = false)
    private String animal;
    @Column(nullable = false)
    private String proposedConfig;
    @Column(nullable = false)
    private String actualConfig;

    public Game(Player player, LocalDate startDate, int tries, String animal, String proposedConfig, String actualConfig) {
        this.player = player;
        this.startDate = startDate;
        this.tries = tries;
        this.animal = animal;
        this.proposedConfig = proposedConfig;
        this.actualConfig = actualConfig;
    }

    public Game() {

    }

    public Game(Player player, LocalDate startDate, String animal) {
        this.player = player;
        this.startDate = startDate;
        this.animal = animal;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getProposedConfig() {
        return proposedConfig;
    }

    public void setProposedConfig(String proposedConfig) {
        this.proposedConfig = proposedConfig;
    }

    public String getActualConfig() {
        return actualConfig;
    }

    public void setActualConfig(String actualConfig) {
        this.actualConfig = actualConfig;
    }
}

