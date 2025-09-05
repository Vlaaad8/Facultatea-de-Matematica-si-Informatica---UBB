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

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
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
    public TableColumn<Game, String> colText;
    public Label warningText;
    private IServices service;
    private Player player;
    long startTime;
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private Button[][] buttons = new Button[3][3];
    private String[][] values = new String[3][3];
    private boolean gameRunning = false;
    private int triesCount = 0;
    private Configuration config;

    public void setService(IServices service) {
        this.service = service;
    }

    public void setPlayer(Player player) {
        this.player = player;
        initializeButtons();
        populateTale();
    }

    public void initialize() {
        colUsername.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPlayer().getName()));
        colPuncte.setCellValueFactory(new PropertyValueFactory<>("points"));
        colDurata.setCellValueFactory(new PropertyValueFactory<>("seconds"));
        tabelClasament.setItems(model);
    }

    public void populateTale() {
        Iterable<Game> games = service.getGames();
        List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
        model.setAll(shownFlights);
    }


    public void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(100, 100);
                buttons[i][j].setStyle("-fx-background-color: #4CAF50; -fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px ; -fx-text-fill: white;");
                final int row = i;
                final int col = j;

                buttons[i][j].setOnMouseClicked(event -> {
                    if (gameRunning) {
                        if (triesCount < 9) {
                            buttons[row][col].setText("X");
                            values[row][col] = "X";
                            updateConfig();
                            triesCount++;
                            System.out.println(triesCount);
                            //buttons[row][col].setDisable(true);
                            String status = checkFinished();
                            if (status.equals("X")) {
                                System.out.println("A castigat X");
                                finishGame(status);
                            }
                            //Draw
                            else if(triesCount==9) {
                                System.out.println("Am ajuns aici!");
                                finishGame("-");
                            }
                            else {
                                calculatorMove();
                                triesCount++;
                                status = checkFinished();
                                if (status.equals("0")) {
                                    System.out.println("A castigat 0");
                                    finishGame(status);
                                }
                            }
                            //warningText.setText("Round " + triesCount + " out of 4");
                        }
                    }

                });
                gridPane.add(buttons[i][j], j, i);

            }
        }
    }


    public void initializeValues() {
        String table = config.getConfig();
        char[] letters = table.toCharArray();
        for (int i = 0; i < letters.length; i++) {
            values[i / 3][i % 3] = String.valueOf(letters[i]);
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(values[i][j].equals("_")){
                    buttons[i][j].setText("");
                }
                else if(values[i][j].equals("X")){
                    buttons[i][j].setText("X");
                }
                else{
                    buttons[i][j].setText("0");
                }
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

    public void finishGame(String status) {
        int points;
        if(status.equals("X")){
            warningText.setText("X wins");
            points=10;
        }
        else if(status.equals("0")){
            warningText.setText("0 wins");
            points=-10;
        }
        else{
            warningText.setText("Draw");
            points=5;
        }
        long finishTime = System.currentTimeMillis();
        finishTime=(finishTime-startTime)/1000;
        lblStatus.setText("Game Finished!");
        Game game = new Game(player,(int)finishTime,points,config.getConfig());
        gameRunning = false;
        config.setId(0);
        service.saveConfig(config);
        game.setId(0);
        service.saveGame(game);
    }

    public void handleStart(ActionEvent actionEvent) {
        config = new Configuration("_________");
        gameRunning = true;
        triesCount = 0;
        lblStatus.setText("Start!");
        initializeValues();
        startTime=System.currentTimeMillis();


    }

    public void updateConfig() {
        String aux = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                aux += values[i][j];
            }
        }
        config.setConfig(aux);
    }

    public void calculatorMove() {
        char[] letters=config.getConfig().toCharArray();
        boolean search=true;
        do{
            Random random = new Random();
            int randomV=random.nextInt(letters.length);
            if(letters[randomV]=='_'){
                letters[randomV]='0';
                search=false;
            }
        }while(search);
        config.setConfig(String.valueOf(letters));
        initializeValues();


    }

    public String checkFinished() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (values[i][0].equals(values[i][1]) && values[i][0].equals(values[i][2]) && !values[i][0].equals("_")) {
                    return values[i][0];
                }
                if (values[0][j].equals(values[1][j]) && values[0][j].equals(values[2][j]) && !values[0][j].equals("_")) {
                    return values[0][j];
                }
            }
        }
        if (values[0][0].equals(values[1][1]) && values[0][0].equals(values[2][2]) && !values[0][0].equals("_")) {
            return values[0][0];
        }
        else if (values[0][2].equals(values[1][1]) && values[0][2].equals(values[2][0]) && !values[0][2].equals("_")) {
            return values[0][2];
        }
        return "-";
    }
}



