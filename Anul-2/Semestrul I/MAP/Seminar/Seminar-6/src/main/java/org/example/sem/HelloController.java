package org.example.sem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.*;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Button buttonOk;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}