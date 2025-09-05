package org.example.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GameDTO implements Serializable {
    private int id;
    private PlayerDTO player;
    private int guessedWords;
    private int points;
    private LocalDateTime time;
    public GameDTO(int id, PlayerDTO player, int points, int guessedWords,LocalDateTime time) {
        this.id = id;
        this.player = player;
        this.guessedWords = guessedWords;
        this.points = points;
        this.time = time;
    }


    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuessedWords() {
        return guessedWords;
    }

    public void setGuessedWords(int guessedWords) {
        this.guessedWords = guessedWords;
    }

    public int getPoints() {
        return points;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
