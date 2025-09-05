package org.example.restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.restaurant.domain.Table;
import org.example.restaurant.repository.*;
import org.example.restaurant.service.*;
import org.example.restaurant.views.EmployeeView;
import org.example.restaurant.views.TableViewApp;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    private RepositoryEmployee repositoryEmployee;
    private RepositoryTable repositoryTable;
    private RepositoryMenuItem repositoryMenuItem;
    private RepositoryOrder repositoryOrder;
    private RepositoryOrderItem repositoryOrderItem;
    private ServiceEmployee serviceEmployee;
    private ServiceTable serviceTable;
    private ServiceMenuItems serviceMenuItems;
    private ServiceOrder serviceOrder;
    private ServiceOrderItem serviceOrderItem;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        String username = "postgres";
        String password = "1mai1984";
        String url = "jdbc:postgresql://localhost:5432/postgres";

        repositoryEmployee = new RepositoryEmployee(username, password, url);
        repositoryTable = new RepositoryTable(username, password, url);
        repositoryMenuItem = new RepositoryMenuItem(username, password, url);
        repositoryOrder = new RepositoryOrder(username, password, url);
        repositoryOrderItem = new RepositoryOrderItem(username, password, url);
        serviceEmployee = new ServiceEmployee(repositoryEmployee);
        serviceTable = new ServiceTable(repositoryTable);
        serviceMenuItems = new ServiceMenuItems(repositoryMenuItem);
        serviceOrder = new ServiceOrder(repositoryOrder);
        serviceOrderItem = new ServiceOrderItem(repositoryOrderItem,repositoryMenuItem);
        initTables();
        initEmployee();
    }

    private void initTables() throws IOException, SQLException {
        for (Table table : serviceTable.findAll()) {
            Stage primaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("table-view.fxml"));
            AnchorPane userLayout = fxmlLoader.load();
            primaryStage.setScene(new Scene(userLayout));
            primaryStage.setTitle("Table " + table.getId());
            TableViewApp tableView = fxmlLoader.getController();
            tableView.setService(serviceMenuItems,serviceOrderItem,serviceOrder,table);
            Scene scene = new Scene(tableView.getvBox(),400,300);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    private void initEmployee() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("employee-view.fxml"));

        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        Stage stage2 = new Stage();
        stage2.setScene(scene);
        stage2.setTitle("Staff");

        EmployeeView requestView = loader.getController();
        requestView.setService(serviceEmployee);
        stage2.show();
    }
}