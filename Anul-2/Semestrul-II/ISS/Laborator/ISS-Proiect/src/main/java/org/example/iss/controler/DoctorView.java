package org.example.iss.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.iss.domain.Drug;
import org.example.iss.domain.Order;
import org.example.iss.domain.User;
import org.example.iss.service.Service;

import java.util.List;

public class DoctorView {
    @FXML
    public TableView<Drug> tableView;
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
    public TextArea orderText;
    @FXML
    public Button specialOrderButton;
    @FXML
    public Label doctorLabel;
    @FXML
    public Button orderButton;
    ObservableList<Drug> model = FXCollections.observableArrayList();
    private Service service;
    private User user;

    public void setService(Service service,User user) {
        this.service = service;
        this.user = user;
        initMain();
    }
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        obsColumn.setCellValueFactory(new PropertyValueFactory<>("observations"));
        unitsColumn.setCellValueFactory(new PropertyValueFactory<>("availableUnits"));
        tableView.setItems(model);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    public void initMain(){
        List<Drug> drugs=service.findAll();
        model.clear();
        model.addAll(drugs);
    }

    public void handleAdd(ActionEvent actionEvent) {
        ObservableList<Drug> observableList=tableView.getSelectionModel().getSelectedItems();
        int orderID=service.saveOrder(user,observableList.size());
        Order order=new Order(user,observableList.size());
        order.setId(orderID);
        observableList.forEach(drug->{
            service.saveOrderItem(drug,order);
            drug.setAvailableUnits(drug.getAvailableUnits()-1);
            service.update(drug.getId(), drug.getName(), drug.getType(), drug.getPrice(), drug.getObservations(), drug.getAvailableUnits()-1);
        });
        initMain();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Order");
        alert.setHeaderText(null);
        alert.setContentText("Order added");
        alert.showAndWait();
    }

    public void handleAddSpecial(ActionEvent actionEvent) {
        String order = orderText.getText();
        service.saveSpecialOrder(order);
        orderText.clear();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Special Order");
        alert.setHeaderText(null);
        alert.setContentText("Order created successfully!");
        alert.showAndWait();

    }
}
