package org.example.DTO;

import org.example.Player;

import java.io.Serializable;
import java.time.LocalDate;

public class GameDTO implements Serializable {
    private int id;
    private PlayerDTO player;
    private int points;
    private int seconds;
    private String configuration;
    private String proposedMoves;

    public GameDTO(int id, PlayerDTO player, int points, int seconds,String configuration,String proposedMoves) {
        this.id = id;
        this.player = player;
        this.points = points;
        this.seconds = seconds;
        this.configuration = configuration;
        this.proposedMoves = proposedMoves;
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
