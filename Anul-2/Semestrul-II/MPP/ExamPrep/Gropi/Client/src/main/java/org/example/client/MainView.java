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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public TableColumn<Game, Integer> colDurata;
    public ObservableList<Game> model = FXCollections.observableArrayList();
    public Label warningText;
    private IServices service;
    private Player player;
    @FXML
    private Button[][] buttons = new Button[5][5];
    private int[][] values = new int[5][5];
    private boolean gameRunning = false;
    private int currentColumn;
    private long startTime;
    private long endTime;
    private List<Configuration> config = new ArrayList<>(6);
    private int points;
    private String proposedMoves;
    private String currentConfig="";

    public void setService(IServices service) {
        this.service = service;
    }

    public void setPlayer(Player player) {
        this.player = player;
        initializeValues();
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


    public void initializeValues() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                values[i][j] = 0;
            }
        }
        Random rand1 = new Random();
        int number = rand1.nextInt(2);

        for (int i = 0; i < 5; i++) {
            Configuration configuration = service.getRandomConfig(i, number);
            config.add(configuration);
        }
        Random rand = new Random();
        int randomColumn = rand.nextInt(5);
        Configuration configuration = service.getRandomConfig(rand.nextInt(5), number);
        config.add(configuration);

        for(Configuration configuration1:config) {
            values[configuration1.getI()][configuration1.getJ()] = 1;
            currentConfig += "("+configuration1.getI()+" "+configuration1.getJ()+") ";
        }
    }

    public void initializeButtons() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(100, 100);
                buttons[i][j].setStyle("-fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px ; -fx-text-fill: black;");
                final int row = i;
                final int col = j;
                buttons[i][j].setOnMouseClicked(event -> {
                            if (gameRunning) {
                                if (currentColumn < 5) {
                                    if (col == currentColumn) {
                                        if (values[row][col] == 0) {
                                            buttons[row][col].setText("Next");
                                            PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                            pause.setOnFinished(e -> buttons[row][col].setText(""));
                                            pause.play();
                                            proposedMoves+="("+row+" "+col+") ";
                                            points += (col + 1) * 2;
                                            warningText.setText("");
                                            currentColumn++;
                                            warningText.setText("Urmatoarea mutare e pe coloana "+(currentColumn+1));
                                            if(currentColumn == 5) {
                                                finishGame(points);
                                            }
                                        } else {
                                            buttons[row][col].setText("GROAPA");
                                            proposedMoves+="("+row+" "+col+") ";
                                            PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                            pause.setOnFinished(e -> buttons[row][col].setText(""));
                                            pause.play();
                                            finishGame(points);
                                        }
                                    }
                                    else{
                                        warningText.setText("Apasa pe coloana cu numarul "+(currentColumn+1)+"!");
                                    }
                                }
                                else{
                                    finishGame(points);
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

    public void finishGame(int tries) {
        gameRunning = false;
        lblStatus.setText("Game ended with " +points+" points");
        warningText.setText("It's a win!");
        endTime= System.currentTimeMillis();
        long timeToFinish=(endTime-startTime)/1000;
        Game game=new Game(player,points,(int)timeToFinish,currentConfig,proposedMoves);
        game.setId(0);
        service.saveGame(game);
    }

    public void handleStart(ActionEvent actionEvent) {
        gameRunning = true;
        currentColumn = 0;
        points = 0;
        lblStatus.setText("Start!");
        startTime=System.currentTimeMillis();
        proposedMoves="";
    }


}
