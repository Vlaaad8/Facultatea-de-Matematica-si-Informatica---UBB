module org.example.seminar8 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.seminar8 to javafx.fxml;
    exports org.example.seminar8;
}