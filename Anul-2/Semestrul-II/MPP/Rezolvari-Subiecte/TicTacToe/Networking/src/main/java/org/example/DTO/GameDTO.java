package org.example.DTO;

import org.example.Configuration;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GameDTO implements Serializable {
    private int id;
    private PlayerDTO player;
    private int seconds;
    private int points;
    private String configuration;

    public GameDTO(int id, PlayerDTO player, int seconds, int points, String configuration) {
        this.id = id;
        this.player = player;
        this.seconds = seconds;
        this.points = points;
        this.configuration = configuration;
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

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
