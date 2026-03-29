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
        String animal=configuration.getAnimal();
        String image=configuration.getAnimalLink();
        return new ConfigurationDTO(id ,i,j,animal,image);
    }
    public static Configuration getFromDTO(ConfigurationDTO configuration) {
        int id= configuration.getId();
        int i=configuration.getI();
        int j=configuration.getJ();
        String animal=configuration.getAnimal();
        String image=configuration.getAnimalLink();
        Configuration configuration1=new Configuration(i,j,animal,image);
        configuration1.setId(id);
        return configuration1;
    }

    public static GameDTO getDTO(Game configuration) {
        int id= configuration.getId();
        PlayerDTO name=DTOUtils.getDTO(configuration.getPlayer());
        String animal=configuration.getAnimal();
        LocalDate date=configuration.getStartDate();
        int tries=configuration.getTries();
        String config=configuration.getActualConfig();
        String proposedConfig=configuration.getProposedConfig();
        return new GameDTO(id,name,date,tries,animal,proposedConfig,config);

    }
    public static Game getFromDTO(GameDTO configuration) {
        int id= configuration.getId();
        Player name=DTOUtils.getFromDTO(configuration.getPlayer());
        String animal=configuration.getAnimal();
        LocalDate date=configuration.getStartDate();
        int tries=configuration.getTries();
        String config=configuration.getActualConfig();
        String proposedConfig=configuration.getProposedConfig();
        Game game=new Game(name,date,tries,animal,proposedConfig,config);
        game.setId(id);
        return game;
    }

}
