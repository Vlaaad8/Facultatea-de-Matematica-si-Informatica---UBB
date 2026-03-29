package org.example.iss.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.iss.domain.Drug;
import org.example.iss.service.Service;

import java.util.List;

public class AdminView {
    @FXML
    public TableView<Drug> mainTable;
    @FXML
    public TableColumn<Drug, String> nameColumn;
    @FXML
    public TableColumn<Drug, String> typeColumn;
    @FXML
    public TableColumn<Drug, Float> priceColumn;
    @FXML
    public TableColumn<Drug, String> obsColumn;
    @FXML
    public TableColumn<Drug, Integer> unitsColumn;
    @FXML
    public Button addButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    public TextField nameBox;
    public TextField typeBox;
    public TextField priceBox;
    public TextField obsBox;
    public TextField unitsBox;
    public TextField idBox;
    ObservableList<Drug> model = FXCollections.observableArrayList();

    private Service service;
    private int id;

    public void setService(Service service) {
        this.service = service;
        initMain();
    }

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        obsColumn.setCellValueFactory(new PropertyValueFactory<>("observations"));
        unitsColumn.setCellValueFactory(new PropertyValueFactory<>("availableUnits"));
        mainTable.setItems(model);
        mainTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldDrug, selectedDrug) -> {
                    if (selectedDrug != null) {
                        id= selectedDrug.getId();
                        nameBox.setText(selectedDrug.getName());
                        typeBox.setText(selectedDrug.getType());
                        priceBox.setText(Float.toString(selectedDrug.getPrice()));
                        obsBox.setText(selectedDrug.getObservations());
                        unitsBox.setText(Integer.toString(selectedDrug.getAvailableUnits()));
                    } else {
                        nameBox.clear();
                        typeBox.clear();
                        priceBox.clear();
                        obsBox.clear();
                        unitsBox.clear();
                    }
                }
        );


    }

    public void initMain() {
        List<Drug> drugs = service.findAll();
        model.clear();
        model.addAll(drugs);
    }

    public void handleAdd(ActionEvent actionEvent) {
        String name = nameBox.getText();
        String type = typeBox.getText();
        Float price = Float.parseFloat(priceBox.getText());
        String observations = obsBox.getText();
        Integer availableUnits = Integer.parseInt(unitsBox.getText());
        service.add(name, type, price, observations, availableUnits);
        nameBox.clear();
        typeBox.clear();
        priceBox.clear();
        obsBox.clear();
        unitsBox.clear();
        initMain();
    }

    public void handleUpdate(ActionEvent actionEvent) {
        String name = nameBox.getText();
        String type = typeBox.getText();
        Float price = Float.parseFloat(priceBox.getText());
        String observations = obsBox.getText();
        Integer availableUnits = Integer.parseInt(unitsBox.getText());
        service.update(id,name, type, price, observations, availableUnits);
        nameBox.clear();
        typeBox.clear();
        priceBox.clear();
        obsBox.clear();
        unitsBox.clear();
        initMain();
    }

    public void handleDelete(ActionEvent actionEvent) {
        Drug drug = mainTable.getSelectionModel().getSelectedItem();
        if(drug!=null) {
            service.delete(drug);
            initMain();
        }
    }
}
