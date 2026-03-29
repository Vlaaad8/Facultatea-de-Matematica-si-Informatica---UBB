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
        String config=configuration.getConfig();
        return new ConfigurationDTO(id,config);
    }
    public static Configuration getFromDTO(ConfigurationDTO configuration) {
        int id= configuration.getId();
        String config=configuration.getConfiguration();
        Configuration configuration1=new Configuration(config);
        configuration1.setId(id);
        return configuration1;
    }

    public static GameDTO getDTO(Game game) {
        int id = game.getId();
        PlayerDTO player = DTOUtils.getDTO(game.getPlayer());
        int seconds = game.getSeconds();
        int points = game.getPoints();
        String configuration=game.getConfig();

        return new GameDTO(id, player, seconds, points, configuration);
    }
public static Game getFromDTO(GameDTO dto) {
    int id = dto.getId();
    Player player = DTOUtils.getFromDTO(dto.getPlayer());
    int seconds = dto.getSeconds();
    int points = dto.getPoints();
    String config = dto.getConfiguration();

    Game game = new Game(player, seconds, points, config);
    game.setId(id);
    return game;
}


}
