package com.example.controller;

import com.example.utils.DBUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private TextField tf_first_name;

    @FXML
    private TextField tf_last_name;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField tf_password;

    @FXML
    private Button button_signup;

    @FXML
    private Button button_log_in;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_signup.setOnAction(event -> {
            if (!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty() &&
                    !tf_first_name.getText().trim().isEmpty() && !tf_last_name.getText().trim().isEmpty()) {
                LogInController.loggedUsername = tf_username.getText();
                DBUtils.signUpUser(event, tf_first_name.getText(), tf_last_name.getText(), tf_username.getText(), tf_password.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Completeaza toate campurile!");
                alert.show();
                throw new IllegalArgumentException("Completeaza toate campurile!");
            }
        });

        button_log_in.setOnAction(event -> DBUtils.changeScene(event, "/com/example/proiect/log-in.fxml", "Sisyphus-Network", null));
    }
}
