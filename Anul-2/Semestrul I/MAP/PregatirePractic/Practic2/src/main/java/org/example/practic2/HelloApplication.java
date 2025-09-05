package org.example.practic2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.practic2.repository.RepositoryNeed;
import org.example.practic2.repository.RepositoryPerson;
import org.example.practic2.service.ServiceNeed;
import org.example.practic2.service.ServicePerson;
import org.example.practic2.view.RegisterLogin;

import java.io.IOException;

public class HelloApplication extends Application {

    private RepositoryPerson repositoryPerson;
    private RepositoryNeed repositoryNeed;
    private ServicePerson servicePerson;
    private ServiceNeed serviceNeed;


    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String password = "1mai1984";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        repositoryPerson = new RepositoryPerson(username, password,url);
        repositoryNeed = new RepositoryNeed(username,password,url);

        servicePerson = new ServicePerson(repositoryPerson);
        serviceNeed=new ServiceNeed(repositoryNeed,repositoryPerson);

        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-login.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        RegisterLogin tableView = fxmlLoader.getController();
        tableView.setService(servicePerson,serviceNeed);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}