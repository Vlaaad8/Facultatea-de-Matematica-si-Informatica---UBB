package org.example.DTO;

import org.example.Player;

import java.io.Serializable;
import java.time.LocalDate;

public class GameDTO implements Serializable {
    private int id;
    private PlayerDTO player;
    private LocalDate startDate;
    private int tries;
    private String animal;
    private String proposedConfig;
    private String actualConfig;


    public GameDTO(int id, PlayerDTO player, LocalDate startDate, int tries, String animal, String proposedConfig, String actualConfig) {
        this.id = id;
        this.player = player;
        this.startDate = startDate;
        this.tries = tries;
        this.animal = animal;
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
