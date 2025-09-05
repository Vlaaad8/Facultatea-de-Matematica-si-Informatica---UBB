package ubb.scs.map.trenuri.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import ubb.scs.map.trenuri.HelloApplication;
import ubb.scs.map.trenuri.domain.validation.CityValidation;
import ubb.scs.map.trenuri.domain.validation.TrainStationValidation;
import ubb.scs.map.trenuri.repository.CityDBRepository;
import ubb.scs.map.trenuri.repository.TrainStationDBRepository;
import ubb.scs.map.trenuri.service.Service;

import java.io.IOException;

public class MainController {
    private Service service;
    @FXML
    private Button loginButton;

    public void setService(Service service) {
        this.service = service;

    }

    public void handleLogin(ActionEvent actionEvent){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/ubb/scs/map/trenuri/client-window-view.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            ClientController clientController = fxmlLoader.getController();
            clientController.setService(service);
            stage.show();
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
