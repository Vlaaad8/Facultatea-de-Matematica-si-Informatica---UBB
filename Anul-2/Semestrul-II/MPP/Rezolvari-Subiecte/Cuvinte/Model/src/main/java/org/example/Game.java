package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name="games")
public class Game extends org.example.Entity<Integer> {
    @ManyToOne
    private Player player;
    @Column(nullable = false)
    private int points;
    @Column(nullable = false)
    private int guessedWords;
    @Column(nullable = false,columnDefinition ="TIMESTAMP")
    private LocalDateTime daytime;

    public LocalDateTime getDaytime() {
        return daytime;
    }

    public void setDaytime(LocalDateTime daytime) {
        this.daytime = daytime;
    }

    public Game(){

    }

    public Game(Player player, int points,int guessedWords) {
        this.player = player;
        this.points = points;
        this.guessedWords = guessedWords;
    }
    public Game(Player player, int points,int guessedWords,LocalDateTime daytime) {
        this.player = player;
        this.points = points;
        this.guessedWords = guessedWords;
        this.daytime = daytime;
    }
    public Game(Player player){
        this.player = player;
        daytime = LocalDateTime.now();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public int getGuessedWords() {
        return guessedWords;
    }

    public void setGuessedWords(int guessedWords) {
        this.guessedWords = guessedWords;
    }
}
