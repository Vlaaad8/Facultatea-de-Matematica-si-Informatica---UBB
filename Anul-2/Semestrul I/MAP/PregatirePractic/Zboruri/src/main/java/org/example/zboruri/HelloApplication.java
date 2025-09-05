package org.example.zboruri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.zboruri.repository.RepositoryClient;
import org.example.zboruri.repository.RepositoryFlight;
import org.example.zboruri.repository.RepositoryTicket;
import org.example.zboruri.service.ServiceClient;
import org.example.zboruri.service.ServiceFlight;
import org.example.zboruri.service.ServiceTicket;
import org.example.zboruri.views.LoginView;

import java.io.IOException;

public class HelloApplication extends Application {
    RepositoryClient repositoryClient;
    ServiceClient serviceClient;
    ServiceFlight serviceFlight;
    RepositoryFlight repositoryFlight;
    RepositoryTicket repositoryTicket;
    ServiceTicket serviceTicket;
    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String password = "1mai1984";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        repositoryClient = new RepositoryClient(username, password,url);
        serviceClient=new ServiceClient(repositoryClient);
        repositoryFlight=new RepositoryFlight(username,password,url);
        serviceFlight=new ServiceFlight(repositoryFlight);
        repositoryTicket=new RepositoryTicket(username,password,url);
        serviceTicket=new ServiceTicket(repositoryTicket);
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        LoginView tableView = fxmlLoader.getController();
        tableView.setService(serviceClient,serviceFlight,serviceTicket);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}