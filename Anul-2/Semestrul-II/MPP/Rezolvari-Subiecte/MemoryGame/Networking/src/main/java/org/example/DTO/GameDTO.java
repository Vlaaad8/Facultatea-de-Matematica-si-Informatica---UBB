package org.example.DTO;

import org.example.Player;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GameDTO implements Serializable {
    private int id;
    private PlayerDTO player;
    private int points;
    private int seconds;
    private String proposedConfig;
    private String actualConfig;

    public GameDTO(int id, PlayerDTO player, int points, int seconds, String proposedConfig, String actualConfig) {
        this.id = id;
        this.player = player;
        this.points = points;
        this.seconds = seconds;
        this.proposedConfig = proposedConfig;
        this.actualConfig = actualConfig;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
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
}
