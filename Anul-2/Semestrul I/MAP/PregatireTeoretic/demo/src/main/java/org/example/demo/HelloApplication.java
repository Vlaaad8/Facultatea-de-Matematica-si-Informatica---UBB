package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.demo.repository.RepositoryCity;
import org.example.demo.repository.RepositoryLooking;
import org.example.demo.repository.RepositoryTrainStation;
import org.example.demo.service.ServiceCity;
import org.example.demo.service.ServiceLooking;
import org.example.demo.service.ServiceTrainStation;
import org.example.demo.views.WindowView;

import java.io.IOException;

public class HelloApplication extends Application {
    private RepositoryCity repositoryCity;
    private RepositoryTrainStation repositoryTrainStation;
    private RepositoryLooking repositoryLooking;
    private ServiceCity serviceCity;
    private ServiceTrainStation serviceTrainStation;
    private ServiceLooking serviceLooking;

    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String password = "1mai1984";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        repositoryCity = new RepositoryCity(username,password,url);
        repositoryTrainStation = new RepositoryTrainStation(username,password,url);
        repositoryLooking = new RepositoryLooking(username,password,url);

        serviceCity = new ServiceCity(repositoryCity);
        serviceTrainStation = new ServiceTrainStation(repositoryTrainStation,repositoryCity);
        serviceLooking = new ServiceLooking(repositoryLooking);

        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("window-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        primaryStage.setTitle("New Window");
        WindowView tableView = fxmlLoader.getController();
        tableView.setService(serviceCity,serviceTrainStation,serviceLooking);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}