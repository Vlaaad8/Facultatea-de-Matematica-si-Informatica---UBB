package org.example.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GameDTO implements Serializable {
    private int id;
    private PlayerDTO player;
    private int points;
    private String proposedConfig;
    private String serverConfig;

    public GameDTO(int id, PlayerDTO player, int points, String proposedConfig, String serverConfig) {
        this.id = id;
        this.player = player;
        this.points = points;
        this.proposedConfig = proposedConfig;
        this.serverConfig = serverConfig;
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
