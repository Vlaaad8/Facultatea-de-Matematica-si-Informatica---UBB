module org.example.practic2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jfr;


    opens org.example.practic2 to javafx.fxml;
    exports org.example.practic2;
    exports org.example.practic2.view;
    opens org.example.practic2.view to javafx.fxml;
    exports org.example.practic2.service;
    opens org.example.practic2.service to javafx.fxml;
}