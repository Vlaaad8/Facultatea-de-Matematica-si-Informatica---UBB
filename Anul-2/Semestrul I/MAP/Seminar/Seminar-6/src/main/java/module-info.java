module org.example.sem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.sem to javafx.fxml;
    exports org.example.sem;
}