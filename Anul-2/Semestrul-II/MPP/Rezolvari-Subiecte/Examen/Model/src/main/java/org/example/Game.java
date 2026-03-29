package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

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
    private String proposedConfig;
    @Column(nullable = false)
    private String actualConfig;
    @Column(nullable = false)
    private int ghicite;



    public Game(){

    }

    public Game(Player player, int points, int seconds, String proposedConfig, String actualConfig,int ghicite) {
        this.player = player;
        this.points = points;
        this.seconds = seconds;
        this.proposedConfig = proposedConfig;
        this.actualConfig = actualConfig;
        this.ghicite = ghicite;
    }
    public Game(Player player) {
        this.player = player;
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

    public int getGhicite() {
        return ghicite;
    }

    public void setGhicite(int ghicite) {
        this.ghicite = ghicite;
    }
}
