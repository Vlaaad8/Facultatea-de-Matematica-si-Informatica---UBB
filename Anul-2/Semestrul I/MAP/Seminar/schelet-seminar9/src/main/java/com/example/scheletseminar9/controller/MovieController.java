package com.example.scheletseminar9.controller;


import com.example.scheletseminar9.domain.Movie;
import com.example.scheletseminar9.dto.MovieFilterDTO;
import com.example.scheletseminar9.service.MovieService;
import com.example.scheletseminar9.util.event.EntityChangeEvent;
import com.example.scheletseminar9.util.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MovieController implements Observer<EntityChangeEvent> {
    private MovieService movieService;
    private ObservableList<Movie> model = FXCollections.observableArrayList();
    @FXML
    private TableView<Movie> tableView;
    @FXML
    private TableColumn<Movie, Long> tableColumnId;
    @FXML
    private TableColumn<Movie, String> tableColumnTitle;
    @FXML
    private TableColumn<Movie, String> tableColumnDirector;
    @FXML
    private TableColumn<Movie, Integer> tableColumnYear;
    @FXML
    private ComboBox<Integer> filterYear;
    @FXML
    private TextField filterYearAfter;
    @FXML
    private TextField filterDirector;
    @FXML
    private TextField filterTitle;

    private MovieFilterDTO filter = new MovieFilterDTO();

    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
        movieService.addObserver(this);
        initModel();
        initYearsCombo();
    }

    private void initYearsCombo() {
        List<Integer> yearOptions = movieService.getYears();
        yearOptions.add(0, null);
        filterYear.getItems().setAll(yearOptions);
    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableColumnDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        tableColumnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        tableView.setItems(model);

        filterYear.getSelectionModel().selectedItemProperty().addListener(o -> {
            filter.setYear(Optional.ofNullable(filterYear.getSelectionModel().getSelectedItem()));
            initModel();
        });
        filterYearAfter.textProperty().addListener(o -> {
            String yearAfterFilter = filterYearAfter.getText();
            Integer yearAfterFilterInt = null;
            if (yearAfterFilter != null && !yearAfterFilter.isBlank()) {
                try {
                    yearAfterFilterInt = Integer.valueOf(yearAfterFilter);
                } catch (NumberFormatException e) {
                    // TODO
                }
            }
            filter.setYearAfter(Optional.ofNullable(yearAfterFilterInt));
            initModel();
        });
        filterDirector.textProperty().addListener(o -> {
            filter.setDirector(Optional.ofNullable(filterDirector.getText()));
            initModel();
        });
        filterTitle.textProperty().addListener(o -> {
            filter.setTitle(Optional.ofNullable(filterTitle.getText()));
            initModel();
        });
    }

    private void initModel() {
        List<Movie> movieList = StreamSupport.stream(movieService.findAll().spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(movieList);
    }

    public void onDelete(ActionEvent actionEvent) {
        Movie selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Optional<Movie> deleted = movieService.delete(selected.getId());
            if (deleted.isPresent()) {
                //MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Movie has been deleted");
                // initModel();
            }
        } else {
            MessageAlert.showErrorMessage(null, "Select a movie first!");
        }
    }

    @Override
    public void update(EntityChangeEvent event) {
        // we could implement different handlers based on the event type:
        // if ADD: don't reload entire table, just add a new row -> works only if not using pagination
        // if DELETE: don't reload entire table, just delete the row -> works only if not using pagination
        // if UPDATE: don't reload entire table, just modify the row's columns
        initModel();
    }
}


