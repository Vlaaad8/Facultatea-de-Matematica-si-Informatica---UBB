package org.example.seminar8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ServiceManager  serviceManager = new ServiceManager();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 440);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        MainView m=(MainView)fxmlLoader.getController();
        m.setService(serviceManager);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}