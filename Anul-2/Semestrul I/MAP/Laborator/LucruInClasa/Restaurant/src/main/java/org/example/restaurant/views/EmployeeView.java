package org.example.restaurant.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.restaurant.domain.Employee;
import org.example.restaurant.service.ServiceEmployee;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EmployeeView {
    @FXML
    public TableView tabelView;
    @FXML
    public TableColumn numeTabel;
    @FXML
    public TableColumn varstaTabel;

    private ServiceEmployee serviceEmployee;

    ObservableList<Employee> model = FXCollections.observableArrayList();

    public void setService(ServiceEmployee serviceEmployee) throws SQLException {
        this.serviceEmployee = serviceEmployee;
        initModel();
    }

    public void initialize() throws SQLException {
        numeTabel.setCellValueFactory(new PropertyValueFactory<>("name"));
        varstaTabel.setCellValueFactory(new PropertyValueFactory<>("age"));
        tabelView.setItems(model);


    }

    private void initModel() throws SQLException {
        Iterable<Employee> messages = serviceEmployee.findAll();
        List<Employee> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }

}
