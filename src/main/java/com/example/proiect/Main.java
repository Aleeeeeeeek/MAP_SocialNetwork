package com.example.proiect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root=FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/proiect/log-in.fxml")));
        primaryStage.setTitle("Sisyphus-Network");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.getScene().getStylesheets().add("/com/example/proiect/stylesheet.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
