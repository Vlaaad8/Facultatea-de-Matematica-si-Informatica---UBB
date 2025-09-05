package org.example.restaurant.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.restaurant.domain.Employee;
import org.example.restaurant.domain.MenuItem;
import org.example.restaurant.domain.Order;
import org.example.restaurant.domain.Table;
import org.example.restaurant.service.ServiceMenuItems;
import org.example.restaurant.service.ServiceOrder;
import org.example.restaurant.service.ServiceOrderItem;

import java.sql.SQLException;

public class TableViewApp {
    private ServiceMenuItems serviceMenuItems;
    private ServiceOrderItem serviceOrderItem;
    private ServiceOrder serviceOrder;
    private Table table;
    private VBox vBox;//pentru a putea adauga dinamic

    public void setService(ServiceMenuItems serviceMenuItems,ServiceOrderItem serviceOrderItem,ServiceOrder serviceOrder,Table table) throws SQLException {
        this.serviceMenuItems = serviceMenuItems;
        this.serviceOrderItem = serviceOrderItem;
        this.serviceOrder = serviceOrder;
        this.table = table;
        vBox=new VBox();
        initModel();
    }



    public void initModel() throws SQLException {
        serviceMenuItems.getItemsByCategory().forEach((category, menuItem) -> {
            Label label = new Label();
            label.setText(category);

            TableView tableView = new TableView();
            TableColumn item = new TableColumn<>("Item");
            item.setCellValueFactory(new PropertyValueFactory<>("item"));
            TableColumn price = new TableColumn<>("Price");
            price.setCellValueFactory(new PropertyValueFactory<>("price"));
            TableColumn currency = new TableColumn<>("Currency");
            currency.setCellValueFactory(new PropertyValueFactory<>("currency"));
            tableView.getColumns().addAll(item, price, currency);
            ObservableList<MenuItem> model = FXCollections.observableArrayList(menuItem);
            tableView.setItems(model);

            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            Button placeOrderButton = new Button("Place order");
            placeOrderButton.setOnAction(event -> {
                ObservableList<MenuItem> items=tableView.getSelectionModel().getSelectedItems();
                for(MenuItem item1:items){
                    Order entity=serviceOrder.Save((table.getId()));
                    try {
                        serviceOrderItem.Save(serviceOrder.findOne(entity),item1.getId());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            vBox.getChildren().addAll(label, tableView,placeOrderButton);


        });

    }

    public VBox getvBox() {
        return vBox;
    }
}
