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
import java.util.Collections;
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
    public TableColumn<Game, Integer> colDurata;
    public ObservableList<Game> model = FXCollections.observableArrayList();
    public Label warningText;
    public TableColumn<Game,Integer> colTimp;
    public Label configText;
    private IServices service;
    private Player player;
    @FXML
    private Button[][] buttons  = new Button[2][4];
    private String[][] values = new String[2][4];
    private boolean gameRunning = false;
    private int currentRound;
    private int matches;
    private long startTime;
    private Configuration config ;
    private int points;
    private String proposedMoves;
    private String value1;
    private String value2;
    private int auxI;
    private int auxJ;
    private String configTxt;

    private static final Logger logger = LogManager.getLogger();


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
        colDurata.setCellValueFactory(new PropertyValueFactory<>("actualConfig"));
        colTimp.setCellValueFactory(new PropertyValueFactory<>("seconds"));
        tabelClasament.setItems(model);
    }

    public void populateTale() {
        Iterable<Game> games = service.getGames();
        List<Game> shownFlights = StreamSupport.stream(games.spliterator(), false).collect(Collectors.toList());
        model.setAll(shownFlights);
    }


    public void initializeValues() {
        configTxt="";
        List<String> currentConfig = new ArrayList<>();
        config=service.getRandomConfig();
        String[]  words=config.getConfiguration().split(",");
        for (String word : words) {
            currentConfig.add(word);
        }
        Collections.shuffle(currentConfig);


        for(int i = 0; i< currentConfig.size(); i++){
            values[i/4][i%4]= currentConfig.get(i);
            System.out.println(values[i/4][i%4]+" ");
            configTxt+=values[i/4][i%4]+" ";
            if(i==4){
                System.out.println("\n");
                configTxt+="\n";
            }
        }
    }


    public void initializeButtons() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(100, 100);
                buttons[i][j].setStyle("-fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px ; -fx-text-fill: black;");
                final int row = i;
                final int col = j;
                buttons[i][j].setOnMouseClicked(event -> {
                    if (gameRunning) {
                        if (currentRound < 6) {
                                if (value1.isEmpty()) {
                                    value1 = values[row][col];
                                    buttons[row][col].setText(value1);
                                    buttons[row][col].setDisable(true);
                                    proposedMoves += "(" + row + " " + col + ") ";
                                    warningText.setText("");
                                    auxI=row;
                                    auxJ=col;
                                }
                                else  {
                                    value2 = values[row][col];
                                    logger.info("Value 2 = {}", value2);
                                    buttons[row][col].setText(value2);
                                    proposedMoves += "(" + row + " " + col + ") ";
                                    buttons[row][col].setDisable(true);
                                    if(value1.equals(value2)) {
                                        matches++;
                                        points-=1;
                                        warningText.setText("It's a match!");
                                    }
                                    else {
                                        buttons[row][col].setDisable(false);
                                        buttons[auxI][auxJ].setDisable(false);
                                        points+=2;
                                        warningText.setText("Not a match!");
                                        PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                        int i1 = auxI, j1 = auxJ;
                                        int i2 = row, j2 = col;

                                        pause.setOnFinished(e -> {
                                            buttons[i1][j1].setText("");
                                            buttons[i2][j2].setText("");
                                        });

                                        pause.play();
                                    }
                                    value1="";
                                    value2="";
                                    lblStatus.setText("Round "+ (currentRound+1));
                                    currentRound++;
                                }

                                if(matches==4 || currentRound==6){
                                    finishGame(points);
                            }

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
        long endTime = System.currentTimeMillis();
        long timeToFinish=(endTime -startTime)/1000;
        Game game=new Game(player,points,(int)timeToFinish,proposedMoves,config.getConfiguration(),matches);
        game.setId(0);
        lblStatus.setText("End!");
        service.saveGame(game);
        configText.setText(configTxt);

    }

    public void handleStart(ActionEvent actionEvent) {
        clearTable();
        initializeValues();
        configText.setText("");
        gameRunning = true;
        matches=0;
        points = 0;
        currentRound = 0;
        lblStatus.setText("Start!");
        proposedMoves="";
        value1="";
        value2="";
        startTime=System.currentTimeMillis();
    }
    public void clearTable(){
        for(int i=0;i<2;i++){
            for(int j=0;j<4;j++){
                buttons[i][j].setText("");
                buttons[i][j].setDisable(false);
            }
        }
    }
    public void showAllValues(){
        for(int i=0;i<2;i++){
            for(int j=0;j<4;j++){
                buttons[i][j].setText(values[i][j]);
            }
        }
    }

}
