package org.example.client;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.example.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainView implements IObserver {
    @FXML
    public Button startButton;
    @FXML
    public Label lblStatus;
    @FXML
    public TableView<Game> tabelClasament;
    @FXML
    public TableColumn<Game, String> colUsername;
    @FXML
    public TableColumn<Game, Integer> colPuncte;
    @FXML
    public TableColumn<Game, Integer> colDurata;
    public ObservableList<Game> model = FXCollections.observableArrayList();
    public Label warningText;
    public TextArea guessBox;
    public Button guessButton;
    private IServices service;
    private Player player;
    private boolean gameRunning = false;
    private Configuration configuration;
    private Map<String,Boolean> status;
    int currentRound;
    int totalPoints;
    int guessWords;
    private Game game;
    public void setService(IServices service) {
        this.service = service;
    }

    public void setPlayer(Player player) {
        this.player = player;
       populateTale();
    }

    public void initialize() {
        colUsername.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPlayer().getName()));
        colPuncte.setCellValueFactory(new PropertyValueFactory<>("points"));
        colDurata.setCellValueFactory(new PropertyValueFactory<>("daytime"));
        tabelClasament.setItems(model);
        guessButton.setVisible(false);
        warningText.setText("Welcome!");
    }

    public void populateTale() {
        Iterable<Game> games = service.getGames();
        List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
        model.setAll(shownFlights);
    }


    public void initializeValues(){
        status = new HashMap<>();
        String word1=configuration.getWord1();
        String word2=configuration.getWord2();
        String word3=configuration.getWord3();
        String word4=configuration.getWord4();
        status.put(word1,false);
        status.put(word2,false);
        status.put(word3,false);
        status.put(word4,false);
    }

    public int correctLetters(String guess,String word){
        int points=0;
        char[] letterArray = guess.toCharArray();
        char[] wordArray = word.toCharArray();
        int size=Math.min(wordArray.length,letterArray.length);
            for(int j=0;j<size;j++){
                if(letterArray[j]==wordArray[j]){
                    points+=1;
                }
            }

        return points;
    }


    @Override
    public void gameFinished() throws Exception {
        Platform.runLater(() -> {
            Iterable<Game> games = service.getGames();
            List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
            model.setAll(shownFlights);
        });
    }

    public void finishGame() {
        gameRunning = false;
        warningText.setText("Game Finished with "+totalPoints+" points");
        game.setGuessedWords(guessWords);
        game.setPoints(totalPoints);
        game.setId(0);
        service.saveGame(game);
        guessButton.setVisible(false);
        startButton.setVisible(true);
        game=null;
    }

    public void handleStart(ActionEvent actionEvent) {
        gameRunning = true;
        configuration=service.getRandomConfig();
        game=new Game(player);
        initializeValues();
        totalPoints = 0;
        guessWords=0;
        currentRound=0;
        warningText.setText("Start!");
        startButton.setVisible(false);
        guessButton.setVisible(true);
    }


    public void handleGuess(ActionEvent actionEvent) {
        String guess = guessBox.getText();
        int maxPoints=0;
        warningText.setText("Round "+(currentRound+1)+"of 4");
        if(currentRound<3 && gameRunning){
            for(Map.Entry<String,Boolean> entry : status.entrySet()){
                    if(entry.getKey().equals(guess.trim())){
                        if(entry.getValue()){
                            return;
                        }
                        totalPoints+=entry.getKey().length();
                        entry.setValue(true);
                        currentRound++;
                        guessWords++;
                        warningText.setText("Round "+(currentRound+1)+"of 4: Word Guessed!");
                        return;
                    }
                    else{
                        maxPoints=Math.max(maxPoints,correctLetters(guess, entry.getKey()));
                    }
                }
            }

        if(maxPoints==0){
            maxPoints++;
        }
        totalPoints+=maxPoints;
        warningText.setText("Round "+(currentRound+1)+"of 4: Round Points:"+maxPoints);
        currentRound++;
        guessBox.clear();
        if(currentRound==4){
            finishGame();
        }
    }
}
