module com.example.proiect {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.proiect to javafx.fxml;
    exports com.example.proiect;
    exports com.example.controller;
    exports com.example.domain;
    exports com.example.repository;
    opens com.example.domain;
    opens com.example.repository;
    opens com.example.utils to javafx.fxml;
    opens com.example.controller to javafx.fxml;
}