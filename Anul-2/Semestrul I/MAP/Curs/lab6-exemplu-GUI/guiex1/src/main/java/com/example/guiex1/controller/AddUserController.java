package com.example.guiex1.controller;

import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.UtilizatorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class AddUserController {
    @FXML
    public TextField textfieldEmail;
    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldLastName;

    @FXML
    private void initialize() {
    }

    UtilizatorService service;
    Stage dialogStage;

    public void setService(UtilizatorService service, Stage stage) {
        this.service = service;
        this.dialogStage=stage;
    }

    @FXML
    public void handleSave(ActionEvent event) {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String email = textfieldEmail.getText();

        Utilizator utilizator = new Utilizator(firstName, lastName,email);
        try {
            service.addUtilizator(utilizator);
            MessageAlert.showMessage(dialogStage, Alert.AlertType.INFORMATION,"Info","User salvat cu succes");
    }
        catch (Exception e) {
            MessageAlert.showErrorMessage(dialogStage,e.getMessage());
        }
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}
