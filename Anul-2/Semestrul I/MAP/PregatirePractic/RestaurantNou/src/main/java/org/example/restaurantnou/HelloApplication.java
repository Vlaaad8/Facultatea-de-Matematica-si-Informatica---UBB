package org.example.restaurantnou;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.restaurantnou.domain.Staff;
import org.example.restaurantnou.repository.*;
import org.example.restaurantnou.service.*;

import java.io.IOException;

public class HelloApplication extends Application {
    private RepositoryStaff repositoryStaff;
    private RepositoryTable repositoryTable;
    private RepositoryMenuItem repositoryMenuItem;
    private RepositoryOrder repositoryOrder;
    private RepositoryOrderItem repositoryOrderItem;
    private ServiceStaff serviceStaff;
    private ServiceTable serviceTable;
    private ServiceMenuItem serviceMenuItem;
    private ServiceOrder serviceOrder;
    private ServiceOrderItem serviceOrderItem;

    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String password = "1mai1984";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        repositoryStaff=new RepositoryStaff(username,password,url);
        repositoryTable=new RepositoryTable(username,password,url);
        repositoryMenuItem=new RepositoryMenuItem(username,password,url);
        repositoryOrder=new RepositoryOrder(username,password,url);
        repositoryOrderItem=new RepositoryOrderItem(username,password,url);

        serviceStaff=new ServiceStaff(repositoryStaff);
        serviceTable=new ServiceTable(repositoryTable);
        serviceMenuItem=new ServiceMenuItem(repositoryMenuItem);
        serviceOrder=new ServiceOrder(repositoryOrder);
        serviceOrderItem=new ServiceOrderItem(repositoryOrderItem,repositoryMenuItem,repositoryOrder);

        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("staff-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        primaryStage.setTitle("Staff");
        StaffView tableView = fxmlLoader.getController();
        tableView.setService(serviceStaff,serviceOrderItem);
        primaryStage.show();

        repositoryTable.findAll().forEach(x->{
            Stage primaryStage1 = new Stage();
            FXMLLoader fxmlLoader1 = new FXMLLoader(HelloApplication.class.getResource("table-restaurant.fxml"));
            AnchorPane userLayout1 = null;
            try {
                userLayout1 = fxmlLoader1.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            primaryStage1.setScene(new Scene(userLayout1));
            primaryStage1.setTitle("Table "+x.getId());
            TableRestaurant tableView1 = fxmlLoader1.getController();
            tableView1.setService(serviceMenuItem,serviceOrder,serviceOrderItem,x);
            Scene scene = new Scene(tableView1.getvBox(),400,300);
            primaryStage1.setScene(scene);
            primaryStage1.show();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}