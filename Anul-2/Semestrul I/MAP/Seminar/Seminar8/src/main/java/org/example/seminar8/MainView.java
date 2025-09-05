package org.example.seminar8;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainView {
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn temaColumn;
    @FXML
    public TableColumn studentColumn;
    @FXML
    public TableColumn noteColumn;
    @FXML
    public TextField textFieldName;
    @FXML
    public TextField textFieldNota;
    @FXML
    public TextField textFieldTema;
    ObservableList<NotaDto> observableList = FXCollections.observableArrayList();
    private ServiceManager serviceManager;

    public void setService(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        initModel();
    }

    public Iterable<NotaDto> getData() {
        ArrayList<NotaDto> notas = new ArrayList<>();
        for (Nota nota : serviceManager.findAllGrades()) {
            NotaDto n = new NotaDto(nota.getValue(), nota.getTema().getId(), nota.getStudent().getName(), nota.getProfesor());
            notas.add(n);
        }
        return notas;
    }

    public void initialize() {

        temaColumn.setCellValueFactory(new PropertyValueFactory<>("temaID"));
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("nota"));
        textFieldName.textProperty().addListener(o -> handleFilter());
        textFieldTema.textProperty().addListener(o -> handleFilter());
        textFieldNota.textProperty().addListener(o -> handleFilter());

        tableView.setItems(observableList);

    }

    public void initModel() {
        Iterable<NotaDto> messages = getData();
        List<NotaDto> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        observableList.setAll(users);
    }

    private void handleFilter() {
        String name = textFieldName.getText();
        String tema = textFieldTema.getText();
        String nota = textFieldNota.getText();

        if (name.isEmpty() && tema.isEmpty() && nota.isEmpty()) {
            initModel();
            return;
        }

        Predicate<NotaDto> verifyName = o -> o.getStudentName().startsWith(name);
        Predicate<NotaDto> verifyTema = o -> o.getTemaID().startsWith(tema);
        Predicate<NotaDto> verifyNota = o -> {
            if (!Objects.equals(nota, "")) {
                return o.getNota() > Double.parseDouble(nota);
            } else return true;
        };

        List<NotaDto> filteredList = observableList.stream()
                .filter(verifyName.and(verifyTema).and(verifyNota))
                .collect(Collectors.toList());

        observableList.setAll(filteredList);
    }


}
