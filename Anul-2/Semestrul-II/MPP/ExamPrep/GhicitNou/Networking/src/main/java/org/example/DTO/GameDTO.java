package org.example.DTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class GameDTO implements Serializable {
    private int id;
    private PlayerDTO player;
    private int points;
    private LocalDateTime daytime;
    private String proposedConfig;
    private String serverConfig;
    private int same;

    public GameDTO(int id, PlayerDTO player, int points, LocalDateTime daytime, String proposedConfig, String serverConfig,int same) {
        this.id = id;
        this.player = player;
        this.points = points;
        this.daytime = daytime;
        this.proposedConfig = proposedConfig;
        this.serverConfig = serverConfig;
        this.same = same;
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

    public void setSame(int same) {
        this.same = same;
    }

    public int getSame() {
        return same;
    }
}
