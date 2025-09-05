package org.example.DTO;

import org.example.Configuration;
import org.example.Game;
import org.example.Player;

import java.time.LocalDate;
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
        int i=configuration.getI();
        int j=configuration.getJ();
        String text=configuration.getText();
        return new ConfigurationDTO(id ,i,j,text);
    }
    public static Configuration getFromDTO(ConfigurationDTO configuration) {
        int id= configuration.getId();
        int i=configuration.getI();
        int j=configuration.getJ();
        String text=configuration.getText();
        Configuration configuration1=new Configuration(i,j,text);
        configuration1.setId(id);
        return configuration1;
    }

    public static GameDTO getDTO(Game configuration) {
        int id= configuration.getId();
        PlayerDTO player= DTOUtils.getDTO(configuration.getPlayer());
        LocalDateTime daytime=configuration.getDaytime();
        int tries=configuration.getTries();
        String clue=configuration.getClue();
        String proposedConfig=configuration.getProposedConfigs();

        return new GameDTO(id,player,daytime,tries,clue,proposedConfig);

    }
    public static Game getFromDTO(GameDTO configuration) {
        int id= configuration.getId();
        Player player=DTOUtils.getFromDTO(configuration.getPlayer());
        LocalDateTime daytime=configuration.getDaytime();
        int tries=configuration.getTries();
        String clue=configuration.getClue();
        String proposedConfig=configuration.getProposedConfigs();
        Game game=new Game(player,daytime,tries,clue,proposedConfig);
        game.setId(id);
        return game;
    }

}
