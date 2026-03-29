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
    @Column(nullable = false)
    private int points;
    @Column(nullable = false,columnDefinition ="TIMESTAMP")
    private LocalDateTime daytime;
    @Column(nullable = false)
    private String proposedConfig;
    @Column(nullable = false)
    private String serverConfig;
    @Column(nullable = false)
    private int same;

    public Game(){

    }

    public Game(Player player) {
        this.player = player;
        this.daytime = LocalDateTime.now();
    }

    public Game(Player player, int points, LocalDateTime daytime, String proposedConfig, String serverConfig,int same) {
        this.player = player;
        this.points = points;
        this.daytime = daytime;
        this.proposedConfig = proposedConfig;
        this.serverConfig = serverConfig;
        this.same = same;
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

    public LocalDateTime getDaytime() {
        return daytime;
    }

    public void setDaytime(LocalDateTime daytime) {
        this.daytime = daytime;
    }

    public String getProposedConfig() {
        return proposedConfig;
    }

    public void setProposedConfig(String proposedConfig) {
        this.proposedConfig = proposedConfig;
    }

    public String getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(String serverConfig) {
        this.serverConfig = serverConfig;
    }

    public int getSame() {
        return same;
    }

    public void setSame(int same) {
        this.same = same;
    }
}
