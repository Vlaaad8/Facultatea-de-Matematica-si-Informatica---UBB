package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="games")
public class Game extends org.example.Entity<Integer> {
    @ManyToOne
    private Player player;
    @Column(nullable = false,columnDefinition ="TIMESTAMP")
    private LocalDateTime daytime;
    @Column(nullable = false)
    private int tries;
    @Column(nullable = false)
    private String clue;
    @Column(nullable = false)
    private String proposedConfigs;



    public Game() {

    }

    public Game(Player player, LocalDateTime daytime, int tries, String clue, String proposedConfigs) {
        this.player = player;
        this.daytime = daytime;
        this.tries = tries;
        this.clue = clue;
        this.proposedConfigs = proposedConfigs;
    }

    public Game(Player player,String clue) {
        this.player = player;
        this.daytime = LocalDateTime.now();
        this.clue = clue;
    }


    public LocalDateTime getDaytime() {
        return daytime;
    }

    public void setDaytime(LocalDateTime daytime) {
        this.daytime = daytime;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public String getProposedConfigs() {
        return proposedConfigs;
    }

    public void setProposedConfigs(String proposedConfigs) {
        this.proposedConfigs = proposedConfigs;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

