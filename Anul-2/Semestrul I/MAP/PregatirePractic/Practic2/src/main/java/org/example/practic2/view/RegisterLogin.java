package org.example.practic2.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.practic2.domain.Person;
import org.example.practic2.repository.RepositoryPerson;
import org.example.practic2.service.ServiceNeed;
import org.example.practic2.service.ServicePerson;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RegisterLogin {
    public TextField lastnameText;
    public TextField firstnameText;
    public TextField passwordText;
    public TextField usernameText;
    public TextField streetText;
    public TextField townText;
    public TextField streetNumberText;
    public TextField telephoneText;
    public Button registerButton;
    public Button loginButton;
    public ComboBox<String> userBox;

    private ServicePerson servicePerson;
    private ServiceNeed serviceNeed;

    public void setService(ServicePerson servicePerson,ServiceNeed serviceNeed) {
        this.servicePerson = servicePerson;
        this.serviceNeed = serviceNeed;
        initMain();
    }

    void initMain() {
        ObservableList<String> toDestinations = FXCollections.observableArrayList();
        Iterable<String> destinations = servicePerson.getUsernames();
        List<String> users = StreamSupport.stream(destinations.spliterator(), false)
                .collect(Collectors.toList());
        toDestinations.setAll(users);
        userBox.setItems(toDestinations);

    }


    public void handleRegister(ActionEvent actionEvent) throws IOException {
        String lastname = lastnameText.getText();
        String firstname = firstnameText.getText();
        String password = passwordText.getText();
        String username = usernameText.getText();
        String usernameToParse=usernameText.getText();
        String street = streetText.getText();
        String town = townText.getText();
        String streetNumber = streetNumberText.getText();
        String telephone = telephoneText.getText();
        servicePerson.Save(lastname, firstname, username,password, town,street, streetNumber, telephone);
        lastnameText.clear();
        firstnameText.clear();
        passwordText.clear();
        usernameText.clear();
        streetText.clear();
        townText.clear();
        streetNumberText.clear();
        telephoneText.clear();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../makegood-view.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Yahoo Messenger");
        MakegoodView mainMenuView = loader.getController();

        Person person=servicePerson.findPersonByUsername(usernameToParse);
        mainMenuView.setService(servicePerson,serviceNeed,person);
        stage.show();


    }

    public void handleLogin(ActionEvent actionEvent) throws IOException {

        String usernameToParse= userBox.getValue();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../makegood-view.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Yahoo Messenger");
        MakegoodView mainMenuView = loader.getController();
        Person person=servicePerson.findPersonByUsername(usernameToParse);
        mainMenuView.setService(servicePerson,serviceNeed,person);
        stage.show();
    }
}
