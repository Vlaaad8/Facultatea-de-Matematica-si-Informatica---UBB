module org.example.laboratorinclass4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.laboratorinclass4 to javafx.fxml;
    exports org.example.laboratorinclass4;
}