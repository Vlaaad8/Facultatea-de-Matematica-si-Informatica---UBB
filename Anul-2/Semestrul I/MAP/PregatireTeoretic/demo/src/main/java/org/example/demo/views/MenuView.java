package org.example.demo.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.example.demo.domain.Looking;
import org.example.demo.domain.TrainStation;
import org.example.demo.domain.event.LookingEntityChange;
import org.example.demo.observer.Observer;
import org.example.demo.service.ServiceCity;
import org.example.demo.service.ServiceLooking;
import org.example.demo.service.ServiceTrainStation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MenuView implements Observer<LookingEntityChange> {
    public static float PRICE_PER_STATION = 10;
    public CheckBox directBox;
    public ComboBox fromBox;
    public ComboBox toBox;
    public Button searchButton;
    public TableView tableView;
    public TableColumn<String, String> nameColumn;
    public Label viewersLabel;
    ObservableList<String> model = FXCollections.observableArrayList();
    private boolean flagOne = false;
    private boolean flagTwo = false;
    private ServiceCity serviceCity;
    private ServiceTrainStation serviceTrainStation;
    private ServiceLooking serviceLooking;
    private int currentLooking=0;
    String currentDeparture=null;
    String currentArrival=null;

    public void initialize() {
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
    }

    public void setService(ServiceCity serviceCity, ServiceTrainStation serviceTrainStation,ServiceLooking serviceLooking,int number) {
        this.serviceCity = serviceCity;
        this.serviceTrainStation = serviceTrainStation;
        this.serviceLooking = serviceLooking;
        this.currentLooking = number;
        serviceLooking.addObserver(this);
        initMain();
    }

    public void initMain() {
        ObservableList<String> toDestinations = FXCollections.observableArrayList();
        Iterable<String> destinations = serviceCity.destinations();
        List<String> d = StreamSupport.stream(destinations.spliterator(), false)
                .collect(Collectors.toList());
        toDestinations.setAll(d);
        toBox.setItems(toDestinations);
        if (!flagOne) {
            toBox.setValue(toDestinations.get(0));
            flagOne = true;
        }

        fromBox.setItems(toDestinations);
        if (!flagTwo) {
            fromBox.setValue(toDestinations.get(0));
            flagTwo = true;
        }
    }


    public void handleSearch(ActionEvent actionEvent) throws SQLException {
        if (directBox.isSelected() && toBox.getValue() != null && fromBox.getValue() != null) {
            //serviceLooking.delete(serviceLooking.getLastID(currentLooking));
            Looking l=new Looking(fromBox.getValue().toString(),toBox.getValue().toString());
            currentDeparture=l.getDeparture();
            currentArrival=l.getDestination();
            System.out.println(l.getDeparture()+" "+l.getDestination());
            serviceLooking.save(l);
            List<String> rout = serviceTrainStation.findDirectRoutes(fromBox.getValue().toString(), toBox.getValue().toString(), PRICE_PER_STATION);
            List<String> d = StreamSupport.stream(rout.spliterator(), false)
                    .collect(Collectors.toList());
            model.setAll(d);
            tableView.setItems(model);

        }
        else{
            //serviceLooking.delete(serviceLooking.getLastID(currentLooking));
            Looking l=new Looking(fromBox.getValue().toString(),toBox.getValue().toString());
            System.out.println(l.getDeparture()+" "+l.getDestination());
            serviceLooking.save(l);
            currentDeparture=l.getDeparture();
            currentArrival=l.getDestination();
            List<String> route=serviceTrainStation.formatRoute(fromBox.getValue().toString(),toBox.getValue().toString(),PRICE_PER_STATION);
            model.setAll(route);
            tableView.setItems(model);

        }
    }

    @Override
    public void update(LookingEntityChange lookingEntityChange) {
        try {
            System.out.println(currentDeparture+" "+currentArrival);
            viewersLabel.setText("Currently viewing: "+serviceLooking.numberViewers(currentDeparture,currentArrival));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

