package com.example.scheletseminar9;

import com.example.scheletseminar9.config.Config;
import com.example.scheletseminar9.controller.MovieController;
import com.example.scheletseminar9.repository.MovieDBRepository;
import com.example.scheletseminar9.repository.MovieRepository;
import com.example.scheletseminar9.service.MovieService;
import com.example.scheletseminar9.validator.MovieValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MovieApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        String dbUrl = Config.getProperties().getProperty("db.url");
        String dbUsername = Config.getProperties().getProperty("db.username");
        String dbPassword = Config.getProperties().getProperty("db.password");
        MovieRepository movieRepository = new MovieDBRepository(dbUrl, dbUsername, dbPassword);

        MovieService movieService = new MovieService(movieRepository, new MovieValidator());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("movies-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Movies");
        stage.setScene(scene);

        MovieController movieController = fxmlLoader.getController();
        movieController.setMovieService(movieService);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}