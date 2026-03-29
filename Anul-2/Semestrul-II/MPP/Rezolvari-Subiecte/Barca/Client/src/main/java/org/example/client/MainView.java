package org.example.client;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class MainView implements IObserver {
    private static final Logger logger = LogManager.getLogger();
    @FXML
    public GridPane gridPane;
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
    private IServices service;
    private Player player;
    ;
    @FXML
    private Button[][] buttons = new Button[5][5];
    private boolean gameRunning = false;
    private int currentRound;
    private Configuration config;
    private int points;
    private String proposedMoves;
    private String[][] values=new String[5][5];

    public void setService(IServices service) {
        this.service = service;
    }

    public void setPlayer(Player player) {
        this.player = player;
        populateTale();
        initializeValues();
        initializeButtons();
    }

    public void initialize() {
        colUsername.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPlayer().getName()));
        colPuncte.setCellValueFactory(new PropertyValueFactory<>("points"));
        colDurata.setCellValueFactory(new PropertyValueFactory<>("serverConfig"));
        tabelClasament.setItems(model);
    }

    public void populateTale() {
        Iterable<Game> games = service.getGames();
        List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
        model.setAll(shownFlights);
    }


    public void initializeValues() {
        config = service.getRandomConfig();
        String[] value1=config.getValue1().split(",");
        String[] value2=config.getValue2().split(",");
        String[] value3=config.getValue3().split(",");
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                values[i][j]="0";
            }
        }
        values[Integer.parseInt(value1[0])][Integer.parseInt(value1[1])]="B";
        values[Integer.parseInt(value2[0])][Integer.parseInt(value2[1])]="B";
        values[Integer.parseInt(value3[0])][Integer.parseInt(value3[1])]="B";



    }


    public void initializeButtons() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                buttons[i][j] = new Button();
                //buttons[i].setText(data.get(i));
                buttons[i][j].setPrefSize(100, 100);
                buttons[i][j].setStyle("-fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px ; -fx-text-fill: black;");
                final int row = i;
                final int col = j;
                buttons[i][j].setOnMouseClicked(event -> {
                    if (gameRunning) {
                        if (currentRound < 3) {
                            proposedMoves += "(" +row+" "+ col + ") ";
                            if(values[row][col].equals("B")) {
                                points+=5;
                                buttons[row][col].setDisable(true);
                                buttons[row][col].setText("B");
                            }
                            else{
                                String distance=calculateDistance(row,col);
                                buttons[row][col].setText(distance);
                                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                pause.setOnFinished(e -> buttons[row][col].setText(""));
                                pause.play();
                                points-=3;

                            }

                            logger.error(points);

                            currentRound++;
                            if (currentRound == 3) {
                                finishGame();
                                warningText.setText("It's a match!");
                            }
                            lblStatus.setText("Round " + (currentRound + 1));

                        }
                    }
                });
                gridPane.add(buttons[i][j], j, i);
            }
        }
    }


@Override
public void gameFinished() throws Exception {
    Platform.runLater(() -> {
        Iterable<Game> games = service.getGames();
        List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
        model.setAll(shownFlights);
    });
}
    public double euclideanDistance(int i, int j, int x, int y) {
        return Math.sqrt(Math.pow((i - x), 2) + Math.pow((j - y), 2));
    }


public void finishGame() {
    gameRunning = false;
    lblStatus.setText("Game ended with " + points + " points");
    warningText.setText("It's a win!");
    String actualConfig= config.getValue1()+" "+config.getValue2()+" "+config.getValue3();
    Game game=new Game(player,points,proposedMoves,actualConfig);
    game.setProposedConfig(proposedMoves);

    game.setId(0);
    lblStatus.setText("End!");
    service.saveGame(game);
    clearTable();
}

public String calculateDistance(int row,int col){
        double min=100000;
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                if(values[i][j].equals("B")){
                    double distance=euclideanDistance(row,col,i,j);
                    min=Math.min(min,distance);
                }
            }
        }
        return String.valueOf(min);
}

public void handleStart(ActionEvent actionEvent) {
    initializeValues();
    gameRunning = true;
    points = 0;
    currentRound = 0;
    proposedMoves="";
    lblStatus.setText("Start!");
    proposedMoves = "";
}

public void clearTable() {
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
            buttons[i][j].setText("");
            buttons[i][j].setDisable(false);
        }
    }

}
}
