package org.example.client;

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
    private Button[] buttons = new Button[4];
    private boolean gameRunning = false;
    private int currentRound;
    private int same;
    private Configuration config;
    private int points;
    private String proposedMoves;
    private String calculatorMoves;
    private List<String> data;
    private Game game;

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
        colDurata.setCellValueFactory(new PropertyValueFactory<>("daytime"));
        tabelClasament.setItems(model);
    }

    public void populateTale() {
        Iterable<Game> games = service.getGames();
        List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
        model.setAll(shownFlights);
    }


    public void initializeValues() {
        data = new ArrayList<>();
        config = service.getRandomConfig();
        data.add(config.getValue1());
        data.add(config.getValue2());
        data.add(config.getValue3());
        data.add(config.getValue4());


    }


    public void initializeButtons() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button();
            //buttons[i].setText(data.get(i));
            buttons[i].setPrefSize(100, 100);
            buttons[i].setStyle("-fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px ; -fx-text-fill: black;");
            final int row = i;
            buttons[i].setOnMouseClicked(event -> {
                if (gameRunning) {
                    if (currentRound < 4) {
                        buttons[row].setDisable(true);
                        proposedMoves+=data.get(row)+"|";
                        String move = calculatorTurn();
                        calculatorMoves+=move+"|";
                        warningText.setText("Calculator: " +move);
                            String[] dataPlayer = data.get(row).split(",");
                            String[] computerData = move.split(",");

                            int valuePlayer = Integer.parseInt(dataPlayer[1]);
                            int valueComputer = Integer.parseInt(computerData[1]);

                            if (valuePlayer > valueComputer) {
                                points += (valuePlayer + valueComputer);
                            } else if (dataPlayer[0].equals(computerData[0])) {
                                same++;
                                logger.error("Am incredentat same {}",same);
                            }
                            else {
                                points -= valuePlayer;
                            }
                            logger.error(points);
                        logger.error("{} {}", valuePlayer, valueComputer);

                        currentRound++;
                        if (currentRound == 4) {
                            finishGame();
                            warningText.setText("It's a match!");
                        }
                        lblStatus.setText("Round " + (currentRound+1));

                    }
                }
            });
            gridPane.add(buttons[i], i, 0);
        }
    }

    private String calculatorTurn() {

        Random rand = new Random();
        return data.get(rand.nextInt(data.size()));

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
    lblStatus.setText("Game ended with " + points + " points");
    warningText.setText("It's a win!");
    game.setProposedConfig(proposedMoves);
    game.setPoints(points);
    game.setSame(same);
    logger.error("Am pus in game {}",same);
    game.setServerConfig(calculatorMoves);
    game.setId(0);
    lblStatus.setText("End!");
    service.saveGame(game);
    clearTable();
}

public void handleStart(ActionEvent actionEvent) {
    initializeValues();
    showData();
    gameRunning = true;
    points = 0;
    currentRound = 0;
    calculatorMoves="";
    same=0;
    lblStatus.setText("Start!");
    proposedMoves = "";
    game=new Game(player);
}

public void clearTable() {
    for (int i = 0; i < 4; i++) {
            buttons[i].setText("");
            buttons[i].setDisable(false);
        }

}
public void showData(){
        for(int i=0;i<4;i++){
            buttons[i].setText(data.get(i));
        }
}
}
