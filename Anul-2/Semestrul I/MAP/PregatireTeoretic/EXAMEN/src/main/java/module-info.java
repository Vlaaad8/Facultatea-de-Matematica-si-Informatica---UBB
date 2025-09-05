module org.example.examen {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jfr;


    opens org.example.examen to javafx.fxml;
    exports org.example.examen;
    exports org.example.examen.view;
    opens org.example.examen.view to javafx.fxml;
}