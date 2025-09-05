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
import org.example.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class MainView implements IObserver {
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
    public TableColumn<Game, LocalDate> colDurata;
    public ObservableList<Game> model = FXCollections.observableArrayList();
    public TableColumn<Game,String> colText;
    public Label warningText;
    private IServices service;
    private Player player;
    ;
    @FXML
    private Button[][] buttons = new Button[3][4];
    private boolean gameRunning = false;
    private int triesCount = 0;
    private Game game;
    private Configuration config;
    private String proposedConfig;

    public void setService(IServices service) {
        this.service = service;
    }

    public void setPlayer(Player player) {
        this.player = player;
        config = this.service.getRandomConfig();
        initializeButtons();
        populateTale();
    }

    public void initialize() {
        colUsername.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPlayer().getName()));
        colPuncte.setCellValueFactory(new PropertyValueFactory<>("tries"));
        colDurata.setCellValueFactory(new PropertyValueFactory<>("daytime"));
        colText.setCellValueFactory(new PropertyValueFactory<>("clue"));
        tabelClasament.setItems(model);
    }

    public void populateTale() {
        Iterable<Game> games = service.getGames();
        List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
        model.setAll(shownFlights);
    }


    public void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(100, 100);
                buttons[i][j].setStyle("-fx-background-color: #4CAF50; -fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px ; -fx-text-fill: white;");
                final int row = i;
                final int col = j;

                buttons[i][j].setOnMouseClicked(event -> {
                    if (gameRunning) {
                        if (triesCount < 4) {
                            if (row == config.getI() && col == config.getJ()) {
                                buttons[row][col].setText(config.getText());
                                triesCount++;
                                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                pause.setOnFinished(e -> buttons[row][col].setText(""));
                                pause.play();
                                proposedConfig += "(" + row + " " + col + ")";
                                finishGame(triesCount);
                            } else {
                                buttons[row][col].setText(String.valueOf(euclideanDistance(row, col, config.getI(), config.getJ())));
                                triesCount++;
                                proposedConfig += "(" + row + " " + col + ")";
                                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                pause.setOnFinished(e -> buttons[row][col].setText(""));
                                pause.play();
                                if(triesCount==4){
                                    finishGame(10);
                            }
                            warningText.setText("Round "+triesCount+" out of 4");
                            }
                        } else {
                            finishGame(10);
                        }
                    }
                });
                gridPane.add(buttons[i][j], j, i);

            }
        }
    }

public double euclideanDistance(int i, int j, int x, int y) {
    return Math.sqrt(Math.pow((i - x), 2) + Math.pow((j - y), 2));
}

@Override
public void gameFinished() throws Exception {
    Platform.runLater(() -> {
        Iterable<Game> games = service.getGames();
        List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
        model.setAll(shownFlights);
    });
}

public void finishGame(int tries) {
    game.setTries(tries);
    lblStatus.setText("Game Finished!");
    game.setProposedConfigs(proposedConfig);
    if(tries!=10){
        game.setClue(config.getText());
    }
    gameRunning = false;
    game.setId(0);
    service.saveGame(game);
}

public void handleStart(ActionEvent actionEvent) {
    gameRunning = true;
    triesCount = 0;
    lblStatus.setText("Start!");
    proposedConfig="";
    config=service.getRandomConfig();
    game=new Game(player,"");

}
}


