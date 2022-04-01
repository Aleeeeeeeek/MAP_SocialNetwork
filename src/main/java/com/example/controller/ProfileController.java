package com.example.controller;

import com.example.utils.DBUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Button button_all_users;
    @FXML
    private Button button_friends;
    @FXML
    private Button button_friend_requests;
    @FXML
    private Button button_log_out;
    @FXML
    private Label label_welcome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_log_out.setOnAction(event -> DBUtils.changeScene(event, "/com/example/proiect/log-in.fxml", "Sisyphus-Network", null));
        button_all_users.setOnAction(event -> DBUtils.changeSceneGeneric(event, "/com/example/proiect/all-users.fxml", "Sisyphus-Network"));
        button_friends.setOnAction(event -> DBUtils.changeSceneGeneric(event, "/com/example/proiect/friends.fxml", "Sisyphus-Network"));
        button_friend_requests.setOnAction(event -> DBUtils.changeSceneGeneric(event, "/com/example/proiect/friend-requests.fxml", "Sisyphus-Network"));
    }

    public void setUserInformation(String username) {
        label_welcome.setText("Welcome " + username + "!");
    }

}
