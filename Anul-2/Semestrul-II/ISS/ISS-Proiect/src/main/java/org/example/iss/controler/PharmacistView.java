package org.example.iss.controler;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.iss.domain.*;
import org.example.iss.repository.OrderItemRepository;
import org.example.iss.service.Service;

import java.util.List;

public class PharmacistView {
    @FXML
    public TableView<Order> tableView;
    @FXML
    public TableColumn<Order,String> doctorColumn;
    @FXML
    public TableColumn<Order,Integer> quantityColumn;
    @FXML
    public TableColumn<Order, OrderStatus> statusColumn;
    @FXML
    public TableColumn<Order,String> drugColumn;
    @FXML
    public Button acceptButton;
    @FXML
    public Button rejectButton;
    ObservableList<Order> model = FXCollections.observableArrayList();
    private Service service;

    public void setService(Service service) {
        this.service = service;
        initMain();
    }

    public void initialize() {
        doctorColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUser();
            String doctorName = "Dr. " + user.getFirstName() + " " + user.getLastName();
            return new SimpleStringProperty(doctorName);
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Order, OrderStatus>("status"));
        drugColumn.setCellValueFactory((cellData -> {
            Order order = cellData.getValue();
            if(order == null){
                throw new RuntimeException("Null!!!!!!!");
            }
            List<Drug> drugs = service.findDrugByOrder(order);
            String drugString = "";
            for (Drug drug : drugs) {
                drugString += drug.getName() + " ";
            }
            return new SimpleStringProperty(drugString);
        }));
        tableView.setItems(model);
    }

    public void initMain(){
        List<Order> orderList=service.findAllOrders();
        model.clear();
        model.addAll(orderList);
    }

    public void handleAccept(ActionEvent actionEvent) {
        Order order = tableView.getSelectionModel().getSelectedItem();
        if (order.getStatus() == OrderStatus.Pending) {
            order.setStatus(OrderStatus.Done);
            service.update(order);
            initMain();
        }
    }

    public void handleReject(ActionEvent actionEvent) {
        Order order = tableView.getSelectionModel().getSelectedItem();
        if (order.getStatus() == OrderStatus.Pending) {
            order.setStatus(OrderStatus.Rejected);
            service.update(order);
            initMain();
        }
    }
}
