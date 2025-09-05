package ubb.scs.map.trenuri.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ubb.scs.map.trenuri.domain.City;
import ubb.scs.map.trenuri.domain.TrainStation;
import ubb.scs.map.trenuri.service.Service;
import ubb.scs.map.trenuri.utils.observer.Observer;

import java.util.*;

public class ClientController implements Observer {
    private Service service;
    @FXML
    private ComboBox<String> departureCombo;
    @FXML
    private ComboBox<String> destinationCombo;

    @FXML
    private Button cautaButton;

    @FXML
    private CheckBox checkBox;
    @FXML
    private ListView<String> listView;
    @FXML
    private Label textCasuta;

    private String idOm;
    private Double constanta = 10.0;

    private Double calcul(Integer nrStatii) {
        return constanta * nrStatii;
    }

    ObservableList<String> model = FXCollections.observableArrayList();

    @FXML
    public void setService(Service service) {
        this.service = service;
        initModel();
        service.addObserver(this);
        this.idOm = UUID.randomUUID().toString();
    }

    @FXML
    public void initialize() {
        listView.setItems(model);
    }

    private void initModel() {
        setCombo();
        updateCasuta();
    }

    private void updateCasuta() {
        try {
            if (departureCombo.getValue() != null && destinationCombo.getValue() != null) {
                String from = departureCombo.getValue();
                String to = destinationCombo.getValue();
                Integer cnt = service.nrPersoane(from, to);
                textCasuta.setText("SUNT " + cnt.toString() + " PERSOANE CU ACEEASI CAUTARE");
            } else {
                textCasuta.setText("MOMENTAN NU AVETI CAUTARI!");
            }
        } catch (Exception e) {
            System.out.println("A apărut o eroare la actualizarea casutei: " + e.getMessage());
            e.printStackTrace();
            textCasuta.setText("A apărut o eroare!");
        }
    }

    public void setCombo() {
        List<City> citiesList = service.getCities();

        citiesList.sort(Comparator.comparing(City::getID));

        Set<String> from = new HashSet<>();
        Set<String> to = new HashSet<>();
        for (City c : citiesList) {
            from.add(c.getName());
            to.add(c.getName());
        }

        departureCombo.getItems().clear();
        destinationCombo.getItems().clear();
        departureCombo.getItems().addAll(from);
        destinationCombo.getItems().addAll(to);
    }

    public void handleSearch() {
        if (departureCombo.getValue() != null && destinationCombo.getValue() != null) {
            String from = departureCombo.getValue();
            String to = destinationCombo.getValue();

            model.clear();

            if (checkBox.isSelected()) {
                // Căutăm doar rutele directe
                findDirectRoutes(from, to);
            } else {
                // Căutăm toate rutele posibile (indirecte)
                List<String> route = new ArrayList<>();
                findRoutes(from, to, route);
            }

            service.addCautare(idOm, from, to);
            service.notifyObservers();
        }
    }

    private void findDirectRoutes(String from, String to) {
        boolean found = false;

        for (TrainStation ts : service.getTrainStations()) {
            String dep = service.getNameById(ts.getDepartureCityId());
            String dest = service.getNameById(ts.getDestinationCityId());

            if (dep.equals(from) && dest.equals(to)) {
                found = true;
                model.add(dep + " -> " + dest + " PRET: " + calcul(1).toString());
            }
        }

        if (!found) {
            model.add("NU SUNT RUTE DIRECTE DISPONIBILE");
        }
    }

    private void findRoutes(String from, String to, List<String> route) {
        // Adăugăm orașul curent la ruta parcurguta
        route.add(from);

        // Dacă am ajuns la destinație, adăugăm ruta completă
        if (from.equals(to)) {
            model.add(String.join(" -> ", route) + " PRET: " + calcul(route.size() - 1).toString());
        } else {
            // Căutăm toate stațiile care pot fi vizitate din orașul 'from'
            for (TrainStation ts : service.getTrainStations()) {
                String depCity = service.getNameById(ts.getDepartureCityId());
                String destCity = service.getNameById(ts.getDestinationCityId());

                // Dacă orașul curent de plecare este "from" și nu am adăugat deja acest oraș în ruta curentă
                if (depCity.equals(from) && !route.contains(destCity)) {
                    // Continuăm căutarea din orașul curent la destinație
                    findRoutes(destCity, to, route);
                }
            }
        }

        // După ce am explorat toate rutele posibile, eliminăm orașul curent din ruta parcurguta
        route.remove(route.size() - 1);
    }


    @Override
    public void update() {
        initModel();
    }
}
