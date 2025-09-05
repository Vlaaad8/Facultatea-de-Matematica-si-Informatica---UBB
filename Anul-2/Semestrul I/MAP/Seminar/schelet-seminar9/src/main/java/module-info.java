module com.example.scheletseminar9 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.scheletseminar9 to javafx.fxml;
    exports com.example.scheletseminar9;

    exports com.example.scheletseminar9.controller;
    opens com.example.scheletseminar9.controller to javafx.fxml;
    opens com.example.scheletseminar9.domain to javafx.base;
    exports com.example.scheletseminar9.config;
    opens com.example.scheletseminar9.config to javafx.fxml;
}