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
    private String proposedConfig;
    @Column(nullable = false)
    private String serverConfig;
    public Game(){

    }

    public Game(Player player) {
        this.player = player;
    }

    public Game(Player player, int points, String proposedConfig, String serverConfig) {
        this.player = player;
        this.points = points;
        this.proposedConfig = proposedConfig;
        this.serverConfig = serverConfig;
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

}
