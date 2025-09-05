package org.example.examen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.examen.view.NormalView;

import java.io.IOException;

public class HelloApplication extends Application {
    //declarare service si repository


    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String password = "1mai1984";
        String url = "jdbc:postgresql://localhost:5432/postgres";

        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("normal-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        primaryStage.setTitle("A view");
        NormalView tableView = fxmlLoader.getController();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}