module org.example.laborator4iclass {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.context;
    requires spring.core;
    requires org.apache.logging.log4j;
    requires java.sql;


    opens org.example.laborator4iclass to javafx.fxml;
    exports org.example.laborator4iclass;
}