module org.example.restaurantnou {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;
    requires jdk.jfr;


    opens org.example.restaurantnou to javafx.fxml;
    exports org.example.restaurantnou;
}