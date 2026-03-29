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
        String value1=configuration.getValue1();
        String value2=configuration.getValue2();
        String value3=configuration.getValue3();
        return new ConfigurationDTO(id ,value1,value2,value3);
    }
    public static Configuration getFromDTO(ConfigurationDTO configuration) {
        int id= configuration.getId();
        String value1=configuration.getValue1();
        String value2=configuration.getValue2();
        String value3=configuration.getValue3();
        Configuration configuration1=new Configuration(value1,value2,value3);
        configuration1.setId(id);
        return configuration1;
    }

    public static GameDTO getDTO(Game configuration) {
        int id= configuration.getId();
        PlayerDTO player=getDTO(configuration.getPlayer());
        int points=configuration.getPoints();
        String proposedConfig=configuration.getProposedConfig();
        String serverConfig=configuration.getServerConfig();

        return new GameDTO(id,player,points,proposedConfig,serverConfig);

    }
    public static Game getFromDTO(GameDTO configuration) {
        int id= configuration.getId();
        Player player=getFromDTO(configuration.getPlayer());
        int points=configuration.getPoints();
        String proposedConfig=configuration.getProposedConfig();
        String serverConfig=configuration.getServerConfig();
        Game game=new Game(player,points,proposedConfig,serverConfig);
        game.setId(id);
        return game;
    }

}
