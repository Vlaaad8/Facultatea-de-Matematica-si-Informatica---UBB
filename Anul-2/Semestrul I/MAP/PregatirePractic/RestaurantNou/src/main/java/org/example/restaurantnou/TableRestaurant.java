package org.example.restaurantnou;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.restaurantnou.domain.*;
import org.example.restaurantnou.domain.MenuItem;
import org.example.restaurantnou.service.ServiceMenuItem;
import org.example.restaurantnou.service.ServiceOrder;
import org.example.restaurantnou.service.ServiceOrderItem;

import java.time.LocalDateTime;

public class TableRestaurant {
    private ServiceMenuItem serviceMenuItem;
    private ServiceOrder serviceOrder;
    private ServiceOrderItem serviceOrderItem;

    private VBox vBox;
    private Table table;
    public void setService(ServiceMenuItem serviceMenuItem,ServiceOrder serviceOrder,ServiceOrderItem serviceOrderItem,Table table) {
        this.serviceMenuItem = serviceMenuItem;
        this.serviceOrder = serviceOrder;
        this.serviceOrderItem = serviceOrderItem;
        this.table = table;
        vBox = new VBox();
        initModel();
    }

    public VBox getvBox() {
        return vBox;
    }

    public void initModel() {
        serviceMenuItem.sortByCategory().forEach((category,values) ->{
            Label categoryLabel = new Label();
            Button buyButton = new Button("Buy");

            categoryLabel.setText(category);
            TableView tableView = new TableView();
            TableColumn categoryColumn = new TableColumn("Item");
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
            TableColumn<MenuItem,String> priceColumn = new TableColumn("Price");
            priceColumn.setCellValueFactory(cellData -> {
                String completePrice=cellData.getValue().getPrice()+" "+cellData.getValue().getCurrency();
                return new SimpleStringProperty(completePrice != null ? completePrice : "Unknown");});

                tableView.getColumns().addAll(categoryColumn, priceColumn);
                ObservableList<MenuItem> model = FXCollections.observableArrayList(values);
                tableView.setItems(model);
                tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                buyButton.setOnAction(event -> {
                    ObservableList<MenuItem> items=tableView.getSelectionModel().getSelectedItems();
                    Order order=new Order(table.getId(), LocalDateTime.now(), OrderStatus.PLACED);
                    serviceOrder.save(order);
                    Long orderID= serviceOrder.findLastId();
                    for(MenuItem item:items){
                        OrderItem orderItem = new OrderItem(orderID,item.getId());
                        serviceOrderItem.save(orderItem);
                    }

                });
                vBox.getChildren().addAll(categoryLabel,tableView,buyButton);

            });

        }
    }

