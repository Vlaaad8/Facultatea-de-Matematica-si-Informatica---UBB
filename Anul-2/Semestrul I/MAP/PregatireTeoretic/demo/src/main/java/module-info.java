module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.jgrapht.core;
    requires jdk.jfr;


    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
    exports org.example.demo.views;
    opens org.example.demo.views to javafx.fxml;
}