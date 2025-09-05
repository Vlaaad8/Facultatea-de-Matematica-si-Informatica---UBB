package org.example.zboruri.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.zboruri.domain.Client;
import org.example.zboruri.service.ServiceClient;
import org.example.zboruri.service.ServiceFlight;
import org.example.zboruri.service.ServiceTicket;

import java.io.IOException;
import java.sql.SQLException;

public class LoginView {
    @FXML
    public Button loginButton;
    @FXML
    public TextField usernameField;
    ServiceClient serviceClient;
    ServiceFlight serviceFlight;
    ServiceTicket serviceTicket;

    public void setService(ServiceClient serviceClient,ServiceFlight serviceFlight,ServiceTicket serviceTicket) {
        this.serviceClient = serviceClient;
       this.serviceFlight = serviceFlight;
       this.serviceTicket = serviceTicket;
    }


    public void handleLoginButton(ActionEvent actionEvent) throws SQLException, IOException {
        String text=usernameField.getText();
        Client client =serviceClient.findByUsername(text);
        if(client!=null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../zboruri.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Yahoo Messenger");
            Zboruri mainMenuView = loader.getController();
            mainMenuView.setService(serviceFlight,serviceTicket,client);
            stage.show();

        }

    }
}
