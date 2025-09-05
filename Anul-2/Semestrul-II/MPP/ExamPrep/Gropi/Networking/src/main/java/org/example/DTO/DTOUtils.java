package org.example.DTO;

import org.example.Configuration;
import org.example.Game;
import org.example.Player;

import java.time.LocalDate;

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
        int i=configuration.getI();
        int j=configuration.getJ();
        int focusColumn=configuration.getFocusColumn();
        return new ConfigurationDTO(id ,focusColumn,i,j);
    }
    public static Configuration getFromDTO(ConfigurationDTO configuration) {
        int id= configuration.getId();
        int i=configuration.getI();
        int j=configuration.getJ();
        int focusColumn=configuration.getFocusColumn();
        Configuration configuration1=new Configuration(focusColumn,i,j);
        configuration1.setId(id);
        return configuration1;
    }

    public static GameDTO getDTO(Game configuration) {
        int id= configuration.getId();
        PlayerDTO player=getDTO(configuration.getPlayer());
        int points=configuration.getPoints();
        int seconds=configuration.getSeconds();
        String configurations=configuration.getConfiguration();
        String proposedMoves=configuration.getProposedMoves();

        return new GameDTO(id,player,points,seconds,configurations,proposedMoves);

    }
    public static Game getFromDTO(GameDTO configuration) {
        int id= configuration.getId();
        Player player= getFromDTO(configuration.getPlayer());
        int points=configuration.getPoints();
        int seconds=configuration.getSeconds();
        String configurations=configuration.getConfiguration();
        String proposedMoves=configuration.getProposedMoves();
        Game game=new Game(player,points,seconds,configurations,proposedMoves);
        game.setId(id);
        return game;
    }

}
