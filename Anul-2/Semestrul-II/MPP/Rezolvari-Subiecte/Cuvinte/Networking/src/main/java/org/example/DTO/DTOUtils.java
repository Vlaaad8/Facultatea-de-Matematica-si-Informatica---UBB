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
        String letters=configuration.getLetters();
        String word1=configuration.getWord1();
        String word2=configuration.getWord2();
        String word3=configuration.getWord3();
        String word4=configuration.getWord4();
        return new ConfigurationDTO(id ,letters,word1,word2,word3,word4);
    }
    public static Configuration getFromDTO(ConfigurationDTO configuration) {
        int id= configuration.getId();
        String letters=configuration.getLetters();
        String word1=configuration.getWord1();
        String word2=configuration.getWord2();
        String word3=configuration.getWord3();
        String word4=configuration.getWord4();
        Configuration configuration1=new Configuration(letters,word1,word2,word3,word4);
        configuration1.setId(id);
        return configuration1;
    }

    public static GameDTO getDTO(Game configuration) {
        int id= configuration.getId();
        PlayerDTO player=getDTO(configuration.getPlayer());
        int points=configuration.getPoints();
        int words=configuration.getGuessedWords();
        LocalDateTime time=configuration.getDaytime();

        return new GameDTO(id,player,points,words,time);

    }
    public static Game getFromDTO(GameDTO configuration) {
        int id= configuration.getId();
        Player player= getFromDTO(configuration.getPlayer());
        int points=configuration.getPoints();
        int words=configuration.getGuessedWords();
        LocalDateTime time=configuration.getTime();
        Game game=new Game(player,points,words,time);
        game.setId(id);
        return game;
    }

}
