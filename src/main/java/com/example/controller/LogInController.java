package com.example.controller;

import com.example.domain.User;
import com.example.domain.validators.UserValidator;
import com.example.repository.db.UserRepository;
import com.example.repository.memory.InMemoryRepository;
import com.example.utils.DBUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private Button button_log_in;

    @FXML
    private Button button_sign_up;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField tf_password;
    public static String loggedUsername;
    public static Iterable<User> users;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_log_in.setOnAction(event -> {
            InMemoryRepository<Long,User> usersRepo = new UserRepository(new UserValidator(),"jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
            users = usersRepo.findAll();
            DBUtils.logInUser(event, tf_username.getText(), tf_password.getText());
            loggedUsername = tf_username.getText();
        });

        button_sign_up.setOnAction(event -> DBUtils.changeScene(event, "/com/example/proiect/sign-up.fxml", "Sisyphus-Network", null));
    }
}
