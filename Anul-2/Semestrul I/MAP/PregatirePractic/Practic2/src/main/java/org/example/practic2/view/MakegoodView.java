package org.example.practic2.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.practic2.domain.Need;
import org.example.practic2.domain.Person;
import org.example.practic2.domain.event.NeedEntityChange;
import org.example.practic2.observer.Observer;
import org.example.practic2.service.ServiceNeed;
import org.example.practic2.service.ServicePerson;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MakegoodView implements Observer<NeedEntityChange> {
    public TableView tableView1;
    public TableColumn titleColumn;
    public TableColumn descriptionColumn;
    public TableColumn deadlineColumn;
    public TableColumn peopleInNeedColumn;
    public TableColumn peopleToHelpColumn;
    public TableColumn statusColumn;
    public Button solveButton;
    public TableView tableView2;
    public TableColumn titleColumn1;
    public TableColumn descriptionColumn1;
    public TableColumn deadlineColumn1;
    public TableColumn peopleToHelpColumn1;
    public TableColumn peopleInNeedColumn1;
    public TableColumn statusColumn1;
    public TextField titltuBox;
    public TextField descriereBox;
    public DatePicker dateBox;
    public Button saveButton;
    ObservableList<Need> model = FXCollections.observableArrayList();
    ObservableList<Need> model1 = FXCollections.observableArrayList();
    private Person person;
    private ServicePerson  servicePerson;

    public ServiceNeed serviceNeed;

    public void setService(ServicePerson servicePerson,ServiceNeed serviceNeed,Person person) {
        this.servicePerson = servicePerson;
        this.serviceNeed = serviceNeed;
        this.person = person;
        serviceNeed.addObserver(this);
        initMain();
    }


    public void initialize(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("description"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("deadline"));
        peopleInNeedColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("personinneed"));
        peopleToHelpColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("persontosave"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("status"));
        titleColumn1.setCellValueFactory(new PropertyValueFactory<Person, String>("title"));
        descriptionColumn1.setCellValueFactory(new PropertyValueFactory<Person, String>("description"));
        deadlineColumn1.setCellValueFactory(new PropertyValueFactory<Person, String>("deadline"));
        peopleInNeedColumn1.setCellValueFactory(new PropertyValueFactory<Person, String>("personinneed"));
        peopleToHelpColumn1.setCellValueFactory(new PropertyValueFactory<Person, String>("persontosave"));
        statusColumn1.setCellValueFactory(new PropertyValueFactory<Person, String>("status"));
        tableView1.setRowFactory(tv -> {
            TableRow<Need> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null && "Erou gasit!".equals(newItem.getStatus())) {
                    row.setDisable(true);
                    row.setStyle("-fx-opacity: 0.5;");
                } else {
                    row.setDisable(false);
                    row.setStyle("");
                }
            });
            return row;
        });

    }

    public void initMain(){
        Iterable<Need> needs= serviceNeed.findAllByTown(person.getTown(), person.getId());
        List<Need> needs1 = StreamSupport.stream(needs.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(needs1);
        tableView1.setItems(model);

        Iterable<Need> needs2=serviceNeed.findAllByPerson(person.getId());
        List<Need> needs3 = StreamSupport.stream(needs2.spliterator(), false)
                .collect(Collectors.toList());
        model1.setAll(needs3);
        tableView2.setItems(model1);
    }

    public void handleSolve(ActionEvent actionEvent) {
        Need request = (Need) tableView1.getSelectionModel().getSelectedItem();
        request.setPersonToSave(person.getId());
        request.setStatus("Erou gasit!");
        serviceNeed.update(request);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Solve successful");
        alert.show();
        initMain();

    }

    public void saveNeed(ActionEvent actionEvent) {
        String title=titltuBox.getText();
        String description=descriereBox.getText();
        LocalDateTime date = dateBox.getValue().atStartOfDay();
        Need need=new Need(title,description,date,person.getId());
        serviceNeed.Save(need);
        titltuBox.clear();
        descriereBox.clear();
        initMain();
    }

    @Override
    public void update(NeedEntityChange needEntityChange) {
        initMain();
    }
}
