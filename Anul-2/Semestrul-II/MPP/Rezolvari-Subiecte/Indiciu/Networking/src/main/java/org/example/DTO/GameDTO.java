package org.example.DTO;

import org.example.Player;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class GameDTO implements Serializable {
    private int id;
    private PlayerDTO player;
    private LocalDateTime daytime;
    private int tries;
    private String clue;
    private String proposedConfigs;

    public GameDTO(int id,PlayerDTO player, LocalDateTime daytime, int tries, String clue, String proposedConfigs) {
        this.id = id;
        this.player = player;
        this.daytime = daytime;
        this.tries = tries;
        this.clue = clue;
        this.proposedConfigs = proposedConfigs;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
