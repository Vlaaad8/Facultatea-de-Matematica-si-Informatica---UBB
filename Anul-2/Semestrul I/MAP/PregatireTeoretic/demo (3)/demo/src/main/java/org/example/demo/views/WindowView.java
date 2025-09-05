package org.example.demo.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.demo.HelloApplication;
import org.example.demo.service.ServiceCity;
import org.example.demo.service.ServiceLooking;
import org.example.demo.service.ServiceTrainStation;

import java.io.IOException;

public class WindowView {
    public Button newWindow;
    private ServiceCity serviceCity;
    private ServiceTrainStation serviceTrainStation;
    private ServiceLooking serviceLooking;

    public void setService(ServiceCity serviceCity,ServiceTrainStation serviceTrainStation,ServiceLooking serviceLooking) {
        this.serviceCity = serviceCity;
        this.serviceTrainStation = serviceTrainStation;
        this.serviceLooking = serviceLooking;
    }

    public void handleWindow(ActionEvent actionEvent) throws IOException {

        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        primaryStage.setTitle("Apele Romane");
        MenuView tableView = fxmlLoader.getController();
        tableView.setService(serviceCity,serviceTrainStation,serviceLooking);
        primaryStage.show();
    }
}
