package org.example.apeleromane.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.apeleromane.domain.City;
import org.example.apeleromane.domain.River;
import org.example.apeleromane.domain.event.RiverEntityChange;
import org.example.apeleromane.observer.Observer;
import org.example.apeleromane.service.ServiceCity;
import org.example.apeleromane.service.ServiceRiver;

import java.security.Provider;

public class WarningsView implements Observer<RiverEntityChange> {

    private ServiceCity serviceCity;
    private ServiceRiver serviceRiver;
    private VBox vbox;
    public void setService(ServiceCity serviceCity,ServiceRiver serviceRiver) {
        this.serviceCity = serviceCity;
        this.serviceRiver = serviceRiver;
        serviceRiver.addObserver(this);
        vbox = new VBox();
        initMain();
    }

    public void initMain() {
        vbox.getChildren().clear();
        serviceCity.sortByRisk().forEach((risk, values) -> {
            Label riskLabel = new Label();
            riskLabel.setText(risk);
            TableView tableView = new TableView();

            TableColumn nameColumn = new TableColumn("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<City, String> riverColumn = new TableColumn("River");
            riverColumn.setCellValueFactory(cellData -> {
                Long senderId = cellData.getValue().getRiver();
                River river = serviceRiver.findOne(senderId);
                return new SimpleStringProperty(river != null ? river.getName() : "Unknown");
            });
            TableColumn minimumRiskColumn = new TableColumn("Minimum Risk");
            minimumRiskColumn.setCellValueFactory(new PropertyValueFactory<>("minimumrisk"));

            TableColumn maximumRiskColumn = new TableColumn("Maximum Risk");
            maximumRiskColumn.setCellValueFactory(new PropertyValueFactory<>("maximumrisk"));

            tableView.getColumns().addAll(nameColumn, riverColumn, minimumRiskColumn, maximumRiskColumn);
            ObservableList<City> model = FXCollections.observableArrayList(values);
            tableView.setItems(model);
            vbox.getChildren().addAll(riskLabel,tableView);

        });
    }

    public VBox getVbox() {
        return vbox;
    }

    @Override
    public void update(RiverEntityChange riverEntityChange) {
        initMain();
    }
}
