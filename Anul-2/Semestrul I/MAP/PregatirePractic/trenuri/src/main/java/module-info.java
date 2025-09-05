module ubb.scs.map.trenuri {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.jgrapht.core;
    requires jdk.jfr;
    requires java.desktop;

    exports ubb.scs.map.trenuri;
    opens ubb.scs.map.trenuri.controller to javafx.fxml;
}