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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private IServices service;
    private Player player;
    @FXML
    private Button[][] buttons = new Button[3][4];
    private String[][] values = new String[3][4];
    private boolean gameRunning = false;
    private int triesCount = 0;
    private Game game;
    private String proposedConfig;

    public ObservableList<Game> model = FXCollections.observableArrayList();
    private Configuration config;

    public void setService(IServices service) {
        this.service = service;
    }

    public void setPlayer(Player player) {
        this.player = player;
        config = this.service.getRandomConfig();
        initializeValues();
        initializeButtons();
        populateTale();
    }

    public void initialize() {
        colUsername.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPlayer().getName()));
        colPuncte.setCellValueFactory(new PropertyValueFactory<>("tries"));
        colDurata.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tabelClasament.setItems(model);
    }

    public void populateTale(){
        Iterable<Game> games = service.getGames();
        List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
        model.setAll(shownFlights);
    }


    public void initializeValues() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                values[i][j] = "Forest";
            }
            values[config.getI()][config.getJ()] = config.getAnimal();

        }
        values[config.getI()][config.getJ()] = config.getAnimal();
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
                        if (triesCount < 3) {
                            buttons[row][col].setText(values[row][col]);
                            triesCount++;
                            proposedConfig+="("+row+" "+col+")";
                            PauseTransition pause = new PauseTransition(Duration.seconds(2));
                            pause.setOnFinished(e -> buttons[row][col].setText(""));
                            pause.play();
                            if (values[row][col].equals(config.getAnimal())) {
                                buttons[row][col].setStyle("");
                                buttons[row][col].setStyle("-fx-background-image: url("+config.getAnimalLink()+"); -fx-background-size: 100px 100px; fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px ; -fx-text-fill: white;");
                                game.setTries(triesCount);
                                lblStatus.setText("Win!");
                                finishGame(triesCount);
                            } else {
                                getDirection(row, col);
                            }
                        } else {
                            finishGame(-1);
                        }
                    }
                });
                gridPane.add(buttons[i][j], j, i);

            }
        }
    }

    @Override
    public void gameFinished() throws Exception {
        Platform.runLater(()->{
            Iterable<Game> games = service.getGames();
            List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
            model.setAll(shownFlights);
        });
    }

    public void finishGame(int tries) {
        game.setTries(tries);
        gameRunning = false;
        game.setId(0);
        String actualConfig="("+config.getI()+" "+config.getJ()+")";
        game.setActualConfig(actualConfig);
        game.setProposedConfig(proposedConfig);
        service.saveGame(game);
        game = null;
        //buttons[config.getI()][config.getJ()].setStyle("-fx-background-color: #4CAF50; -fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px ; -fx-text-fill: white;");
    }

    public void handleStart(ActionEvent actionEvent) {
        buttons[config.getI()][config.getJ()].setStyle("-fx-background-color: #4CAF50; -fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px ; -fx-text-fill: white;");
        config=service.getRandomConfig();
        initializeValues();
        gameRunning = true;
        triesCount = 0;
        lblStatus.setText("Start!");
        game = new Game(player, LocalDate.now(), config.getAnimal());
        proposedConfig="";
    }

    public void getDirection(int i, int j) {
        int iC = config.getI();
        int jC = config.getJ();

        if (i + 1 == iC && j == jC) {
            lblStatus.setText("South");
        } else if (i - 1 == iC && j == jC) {
            lblStatus.setText("North");
        } else if (i == iC && j + 1 == jC) {
            lblStatus.setText("East");
        } else if (i == iC && j - 1 == jC) {
            lblStatus.setText("West");
        } else if (i - 1 == iC && j + 1 == jC) {
            lblStatus.setText("North-East");
        } else if (i + 1 == iC && j - 1 == jC) {
            lblStatus.setText("South-West");
        } else if (i + 1 == iC && j + 1 == jC) {
            lblStatus.setText("South-East");
        } else if (i - 1 == iC && j - 1 == jC) {
            lblStatus.setText("North-West");
        } else {
            lblStatus.setText("Unknown direction");
        }
    }

}
