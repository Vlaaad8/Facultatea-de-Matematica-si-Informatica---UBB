package org.example.apeleromane.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.example.apeleromane.domain.River;
import org.example.apeleromane.domain.event.RiverEntityChange;
import org.example.apeleromane.observer.Observer;
import org.example.apeleromane.service.ServiceRiver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RiverView implements Observer<RiverEntityChange>{
    public TableView<River> tableView;
    public TableColumn<River,String> nameColumn;
    public TableColumn<River,Integer> capacityColumn;
    private ServiceRiver serviceRiver;

    ObservableList<River> model = FXCollections.observableArrayList();

    public void setService(ServiceRiver serviceRiver) {
        this.serviceRiver = serviceRiver;
        serviceRiver.addObserver(this);
        initModel();
    }

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        tableView.setEditable(true);

        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        capacityColumn.setOnEditCommit(event -> {
            River river = event.getRowValue();
            int newCapacity = event.getNewValue();
            river.setCapacity(newCapacity);
            serviceRiver.update(river);


        });
    }

    public void initModel(){
        Iterable<River> rivers=serviceRiver.findAll();
        List<River> river = StreamSupport.stream(rivers.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(river);
        tableView.setItems(model);
    }


    @Override
    public void update(RiverEntityChange riverEntityChange) {
        initModel();
    }
}
