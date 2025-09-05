module org.example.zboruri {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jdk.jfr;


    opens org.example.zboruri to javafx.fxml;
    exports org.example.zboruri;
    exports org.example.zboruri.domain;
    opens org.example.zboruri.domain to javafx.fxml;
    exports org.example.zboruri.views;
    opens org.example.zboruri.views to javafx.fxml;
    exports org.example.zboruri.service;
    opens org.example.zboruri.service to javafx.fxml;
}