package org.example.zboruri.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.zboruri.domain.Client;
import org.example.zboruri.domain.Flight;
import org.example.zboruri.domain.event.FlighEntityChange;
import org.example.zboruri.observer.Observer;
import org.example.zboruri.paging.Page;
import org.example.zboruri.paging.Pageable;
import org.example.zboruri.service.ServiceFlight;
import org.example.zboruri.service.ServiceTicket;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Zboruri implements Observer<FlighEntityChange> {
    public Label usernameBox;
    public ComboBox fromBox;
    public ComboBox toBox;
    public DatePicker dateBox;


    public TableView tableView;
    public TableColumn idColumn;
    public TableColumn toColumn;
    public TableColumn fromColumn;
    public TableColumn departureColumn;
    public TableColumn landingColumn;
    public TableColumn seatsColumn;
    public Button buyButton;
    public TableColumn leftSeats;
    public Button backButton;
    public Button nextButton;
    public Label pageLabel;

    ObservableList<Flight> model = FXCollections.observableArrayList();
    List<String> users;
    List<String> users2;
    List<Flight> users3;
    boolean flagOne = false;
    boolean flagTwo = false;
    private int maxPages;
    private Pageable pageable;
    private Page<Flight> page;

    private ServiceFlight serviceFlight;
    private ServiceTicket serviceTicket;
    private Client client;
    void setService(ServiceFlight serviceFlight,ServiceTicket serviceTicket,Client client) throws SQLException {
        this.serviceFlight = serviceFlight;
        this.serviceTicket = serviceTicket;
        this.client = client;
        usernameBox.setText(client.getUsername());
        serviceFlight.addObserver(this);
        initModel();
        this.pageable=new Pageable(1,1);
        this.page=serviceFlight.findAllOnPage(pageable,fromBox.getValue().toString(),toBox.getValue().toString(),dateBox.getValue().atStartOfDay());


    }

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("departuretime"));
        landingColumn.setCellValueFactory(new PropertyValueFactory<>("landingtime"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));
        leftSeats.setCellValueFactory(new PropertyValueFactory<>("avaibleseats"));
        toBox.valueProperty().addListener(o -> {
            try {
                handleChange();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        fromBox.valueProperty().addListener(o -> {
            try {
                handleChange();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        dateBox.valueProperty().addListener(o -> {
            try {
                handleChange();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void initModel() throws SQLException {
        ObservableList<String> toDestinations = FXCollections.observableArrayList();
        Iterable<String> destinations = serviceFlight.toDestinations();
        users = StreamSupport.stream(destinations.spliterator(), false)
                .collect(Collectors.toList());
        toDestinations.setAll(users);
        toBox.setItems(toDestinations);
        if (!flagOne) {
            toBox.setValue(toDestinations.get(0));
            flagOne = true;
        }
        LocalDate date= LocalDate.parse("2024-01-18");
        dateBox.setValue(date);

        ObservableList<String> fromDestinations = FXCollections.observableArrayList();
        Iterable<String> destinations2 = serviceFlight.fromDestinations();
        users2 = StreamSupport.stream(destinations2.spliterator(), false)
                .collect(Collectors.toList());
        fromDestinations.setAll(users2);
        fromBox.setItems(fromDestinations);

        if (!flagTwo) {
            fromBox.setValue(fromDestinations.get(0));
            flagTwo = true;
        }

    }
        public void handleChange() throws SQLException {
            Iterable<Flight> correctFlights = null;
            if (dateBox.getValue() != null && toBox.getValue()!=null && fromBox.getValue()!=null) {
                try {
                    System.out.println(fromBox.getValue() + " " + toBox.getValue() + " " + dateBox.getValue());
                    correctFlights = serviceFlight.sortDestinationArrivalDate(fromBox.getValue().toString(), toBox.getValue().toString(), dateBox.getValue());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
               users3 = StreamSupport.stream(correctFlights.spliterator(), false)
                        .collect(Collectors.toList());
//                model.clear();
//                model.setAll(users3);
//                tableView.setItems(model);
                maxPages=users3.size()/ pageable.getPageSize();
                handlePageChange();
            }
        }

    public void handleBuyButton(ActionEvent actionEvent) {
        Flight request = (Flight) tableView.getSelectionModel().getSelectedItem();
        System.out.println(request.getSeats());
        serviceTicket.save(client.getUsername(), request.getId(), LocalDateTime.now());
        request.setAvaibleseats(request.getAvaibleseats() -1);
        System.out.println(request.getAvaibleseats());
        try {
            serviceFlight.update(request);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(FlighEntityChange flighEntityChange) {
        try {
            handleChange();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePageChange() throws SQLException {
        System.out.println("page change");
        System.out.println(fromBox.getValue().toString()+" "+toBox.getValue());
        Iterable<Flight> usersOnNewPage = serviceFlight.findAllOnPage(pageable,toBox.getValue().toString(),fromBox.getValue().toString(),dateBox.getValue().atStartOfDay()).getElementsOnPage();
        List<Flight> users4 = StreamSupport.stream(usersOnNewPage.spliterator(), false)
                .collect(Collectors.toList());
        System.out.println(users4.size());
        model.clear();
        model.setAll(users4);
        tableView.setItems(model);
        pageLabel.setText("Page "+pageable.getPageNumber()+" of "+maxPages);
    }


    public void handleBackPage(ActionEvent actionEvent) throws SQLException {
        pageable.setPageNumber(Math.max((pageable.getPageNumber()-1),0));
        page=serviceFlight.findAllOnPage(pageable,fromBox.getValue().toString(),toBox.getValue().toString(),dateBox.getValue().atStartOfDay());
        handlePageChange();
    }

    public void handleNextPage(ActionEvent actionEvent) throws SQLException {
        pageable.setPageNumber(Math.min((pageable.getPageNumber()+1),maxPages));
        page=serviceFlight.findAllOnPage(pageable,fromBox.getValue().toString(),toBox.getValue().toString(),dateBox.getValue().atStartOfDay());
        handlePageChange();
    }
}

