package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Time;

@Entity
@Table(name="games")
public class Game extends org.example.Entity<Integer> {
    @ManyToOne
    private Player player;
    @Column(nullable = false)
    private int points;
    @Column(nullable = false)
    private int seconds;
    @Column(nullable = false)
    private String configuration;
    @Column(nullable = false)
    private String proposedMoves;

    public Game(Player player, int points, int seconds, String configuration,String proposedMoves) {
        this.player = player;
        this.points = points;
        this.seconds = seconds;
        this.configuration = configuration;
        this.proposedMoves = proposedMoves;
    }

    public Game(){

    }

    public Game(Player player) {
        this.configuration = configuration;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getProposedMoves() {
        return proposedMoves;
    }

    public void setProposedMoves(String proposedMoves) {
        this.proposedMoves = proposedMoves;
    }
}
