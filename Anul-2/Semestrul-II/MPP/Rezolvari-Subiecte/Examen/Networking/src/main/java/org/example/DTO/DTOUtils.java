package org.example.DTO;

import org.example.Configuration;
import org.example.Game;
import org.example.Player;

import java.time.LocalDateTime;

public class DTOUtils {
    public static PlayerDTO getDTO(Player playerDTO) {
        int id=playerDTO.getId();
        String name=playerDTO.getName();
        return new PlayerDTO(id,name);
    }
    public static Player getFromDTO(PlayerDTO playerDTO) {
        int id=playerDTO.getId();
        String name=playerDTO.getName();
        Player player=new Player(name);
        player.setId(id);
        return player;
    }

    public static ConfigurationDTO getDTO(Configuration configuration) {
        int id= configuration.getId();
        String config=configuration.getConfiguration();
        return new ConfigurationDTO(id ,config);
    }
    public static Configuration getFromDTO(ConfigurationDTO configuration) {
        int id= configuration.getId();
        String config=configuration.getConfiguration();
        Configuration configuration1=new Configuration(config);
        configuration1.setId(id);
        return configuration1;
    }

    public static GameDTO getDTO(Game configuration) {
        int id= configuration.getId();
        PlayerDTO player=getDTO(configuration.getPlayer());
        int points=configuration.getPoints();
        int seconds=configuration.getSeconds();
        String proposedConfig=configuration.getProposedConfig();
        String actualConfig=configuration.getActualConfig();
        int ghicite= configuration.getGhicite();

        return new GameDTO(id,player,points,seconds,proposedConfig,actualConfig,ghicite);

    }
    public static Game getFromDTO(GameDTO configuration) {
        int id= configuration.getId();
        Player player=getFromDTO(configuration.getPlayer());
        int points=configuration.getPoints();
        int seconds=configuration.getSeconds();
        String proposedConfig=configuration.getProposedConfig();
        String actualConfig=configuration.getActualConfig();
        int ghicite= configuration.getGhicite();
        Game game=new Game(player,points,seconds,proposedConfig,actualConfig,ghicite);
        game.setId(id);
        return game;
    }

}
