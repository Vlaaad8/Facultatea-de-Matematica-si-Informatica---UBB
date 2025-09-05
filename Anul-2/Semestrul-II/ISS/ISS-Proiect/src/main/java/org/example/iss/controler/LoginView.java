package org.example.iss.controler;


import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.example.iss.domain.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.iss.domain.User;
import org.example.iss.service.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Setter
public class LoginView {
    @FXML
    public PasswordField passwordText;
    @FXML
    public TextField usernameText;
    @FXML
    public Button loginButton;
    @FXML
    public Button registerButton;
    @FXML
    public Label errorLabel;

    private Service service;
    private Set<String> loggedClients=new HashSet<>();

    public void setService2(Service service) {
        this.service = service;
    }

    public void handleLogin(ActionEvent actionEvent) throws IOException {
        String username = usernameText.getText();
        String password = passwordText.getText();
        User user = service.login(username, password);
        usernameText.clear();
        passwordText.clear();
        Role role=user.getRole();
        if (!loggedClients.contains(username)) {
            loggedClients.add(username);
            if (role == Role.Admin) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(
                        getClass().getResource("/org/example/iss/adminView.fxml")
                );
                AnchorPane root = (AnchorPane) loader.load();
                AdminView adminView = loader.getController();
                adminView.setService(service);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Admin Panel");
                stage.show();
            }
            if(role==Role.Doctor){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(
                        getClass().getResource("/org/example/iss/doctorView.fxml")
                );
                AnchorPane root = (AnchorPane) loader.load();
                DoctorView adminView = loader.getController();
                adminView.setService(service,user);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Doctor Panel");
                stage.show();
            }
            if(role==Role.Pharmacist){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(
                        getClass().getResource("/org/example/iss/pharmacistView.fxml")
                );
                AnchorPane root = (AnchorPane) loader.load();
                PharmacistView adminView = loader.getController();
                adminView.setService(service);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Pharmacist Panel");
                stage.show();
            }
        }
        else{
            errorLabel.setText("Already logged in");
        }
    }
}
