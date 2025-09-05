module org.example.apeleromane {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jfr;


    opens org.example.apeleromane to javafx.fxml;
    exports org.example.apeleromane;
    exports org.example.apeleromane.view;
    opens org.example.apeleromane.view to javafx.fxml;
}