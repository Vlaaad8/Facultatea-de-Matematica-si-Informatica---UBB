package org.example.apeleromane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.apeleromane.repository.RepositoryCity;
import org.example.apeleromane.repository.RepositoryRiver;
import org.example.apeleromane.service.ServiceCity;
import org.example.apeleromane.service.ServiceRiver;
import org.example.apeleromane.view.RiverView;
import org.example.apeleromane.view.WarningsView;

import java.io.IOException;

public class HelloApplication extends Application {
    private RepositoryRiver repositoryRiver;
    private RepositoryCity repositoryCity;
    private ServiceRiver serviceRiver;
    private ServiceCity serviceCity;

    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String password = "1mai1984";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        repositoryRiver = new RepositoryRiver(username, password,url);
        repositoryCity = new RepositoryCity(username,password,url);
        serviceRiver = new ServiceRiver(repositoryRiver);
        serviceCity = new ServiceCity(repositoryRiver,repositoryCity);

        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("river-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        primaryStage.setTitle("Apele Romane");
        RiverView tableView = fxmlLoader.getController();
        tableView.setService(serviceRiver);
        primaryStage.show();

        Stage primaryStage1 = new Stage();
        FXMLLoader fxmlLoader1 = new FXMLLoader(HelloApplication.class.getResource("warnings-view.fxml"));
        AnchorPane userLayout1 = fxmlLoader1.load();
        primaryStage1.setScene(new Scene(userLayout1));
        primaryStage1.setTitle("Warnings");
        WarningsView tableView1 = fxmlLoader1.getController();
        tableView1.setService(serviceCity,serviceRiver);
        Scene scene = new Scene(tableView1.getVbox(),400,300);
        primaryStage1.setScene(scene);
        primaryStage1.show();

    }

    public static void main(String[] args) {
        launch();
    }
}