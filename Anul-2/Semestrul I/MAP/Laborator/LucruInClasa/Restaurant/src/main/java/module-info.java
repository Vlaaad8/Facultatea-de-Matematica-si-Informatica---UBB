module org.example.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.restaurant to javafx.fxml;
    exports org.example.restaurant;
    exports org.example.restaurant.views;
    opens org.example.restaurant.views to javafx.fxml;
}