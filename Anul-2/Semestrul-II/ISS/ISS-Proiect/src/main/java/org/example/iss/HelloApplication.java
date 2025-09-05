package org.example.iss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.iss.repository.*;
import org.hibernate.SessionFactory;
import org.example.iss.service.Service;
import org.example.iss.controler.LoginView;

public class HelloApplication extends Application {
    static SessionFactory sessionFactory;

    @Override
    public void start(Stage primaryStage) throws Exception {

        UserRepository userRepository = new UserRepository();
        DrugRepository drugRepository = new DrugRepository();
        OrderRepository orderRepository = new OrderRepository();
        SpecialOrderRepository specialOrderRepository = new SpecialOrderRepository();
        OrderItemRepository orderItemRepository = new OrderItemRepository();

        Service service = new Service(userRepository, drugRepository,specialOrderRepository,orderRepository,orderItemRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        primaryStage.setTitle("Yahoo Messenger");
        LoginView loginController = fxmlLoader.getController();
        loginController.setService2(service);
        primaryStage.show();
    }
}
