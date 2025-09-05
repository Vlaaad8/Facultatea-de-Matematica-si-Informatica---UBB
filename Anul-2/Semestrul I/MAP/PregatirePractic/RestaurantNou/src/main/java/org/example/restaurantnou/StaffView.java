package org.example.restaurantnou;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.restaurantnou.domain.OrderMenuDTO;
import org.example.restaurantnou.domain.Staff;
import org.example.restaurantnou.domain.event.OrderEntityChange;
import org.example.restaurantnou.domain.observer.Observer;
import org.example.restaurantnou.service.ServiceOrderItem;
import org.example.restaurantnou.service.ServiceStaff;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class StaffView implements Observer<OrderEntityChange> {
    public TableView tableView;
    public TableColumn idColumn;
    public TableColumn nameColumn;
    public TableView tableView2;
    public TableColumn tableIDColumn;
    public TableColumn dateColumn;
    public TableColumn orderColumn;

    private ServiceStaff serviceStaff;
    private ServiceOrderItem serviceOrderItem;
    ObservableList<Staff> model = FXCollections.observableArrayList();
    ObservableList<OrderMenuDTO> model2 = FXCollections.observableArrayList();

    public void setService(ServiceStaff serviceStaff,ServiceOrderItem serviceOrderItem) {
        this.serviceStaff = serviceStaff;
        this.serviceOrderItem = serviceOrderItem;
        serviceOrderItem.addObserver(this);
        try {
            initModel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableIDColumn.setCellValueFactory(new PropertyValueFactory<>("tableID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderColumn.setCellValueFactory(new PropertyValueFactory<>("items"));
    }

    private void initModel() throws SQLException {
        Iterable<Staff> staff= serviceStaff.findAll();
        List<Staff> staff1= StreamSupport.stream(staff.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(staff1);
        tableView.setItems(model);

        List<OrderMenuDTO> item=serviceOrderItem.showStaffOrder();
        List<OrderMenuDTO> staff2= StreamSupport.stream(item.spliterator(), false)
                .collect(Collectors.toList());
        model2.setAll(staff2);
        tableView2.setItems(model2);
    }

    @Override
    public void update(OrderEntityChange orderEntityChange) {
        try {
            initModel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
