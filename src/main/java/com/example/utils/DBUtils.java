package com.example.utils;

import com.example.controller.MessageController;
import com.example.controller.UsersController;
import com.example.controller.ProfileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;
import java.util.Objects;

public class DBUtils {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                UsersController usersController = loader.getController();
                usersController.setUserInformation(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(DBUtils.class.getResource(fxmlFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        assert root != null;
        stage.setScene(new Scene(root, 600, 400));
        stage.getScene().getStylesheets().add("/com/example/proiect/stylesheet.css");
        stage.show();
    }

    public static void changeSceneGeneric(ActionEvent event, String fxmlFile, String title) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        assert root != null;
        stage.setScene(new Scene(root, 600, 400));
        stage.getScene().getStylesheets().add("/com/example/proiect/stylesheet.css");
        stage.show();
    }

    public static void changeSceneProfile(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                ProfileController profileController = loader.getController();
                profileController.setUserInformation(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(DBUtils.class.getResource(fxmlFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        assert root != null;
        stage.setScene(new Scene(root, 415, 415));
        stage.getScene().getStylesheets().add("/com/example/proiect/stylesheet.css");
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String firstName, String lastName,
                                  String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUsersExists = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
            psCheckUsersExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUsersExists.setString(1, username);
            resultSet = psCheckUsersExists.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("The username is already taken!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The username is already taken!");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (first_name, " +
                        "last_name, username, password) VALUES (?,?,?,?)");
                psInsert.setString(1, firstName);
                psInsert.setString(2, lastName);
                psInsert.setString(3, username);
                String encryptedPassword = PasswordEncrypter.encrypter(password);
                psInsert.setString(4, encryptedPassword);
                psInsert.executeUpdate();
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setContentText("Contul a fost creat!");
                confirmationAlert.show();
                changeSceneGeneric(event, "/com/example/proiect/log-in.fxml", "Sisyphus-Network");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUsersExists != null) {
                try {
                    psCheckUsersExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String ecryptedPassword = PasswordEncrypter.encrypter(password);
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
            preparedStatement = connection.prepareStatement("SELECT password FROM users where username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Utilizatorul nu exista!");
                alert.show();
                throw new IllegalArgumentException("Utilizatorul nu exista!");
            } else {
                resultSet.next();
                String retrievedPassword = resultSet.getString("password");
                if (retrievedPassword.equals(ecryptedPassword)) {
                    changeSceneProfile(event, "/com/example/proiect/profile.fxml", "Sisyphus-Network", username);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Parola introdusa nu este corecta!");
                    alert.show();
                    throw new IllegalArgumentException("Parola introdusa nu este corecta!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void changeSceneMessage(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                MessageController messageController = loader.getController();
                messageController.setText();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(DBUtils.class.getResource(fxmlFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        assert root != null;
        stage.setScene(new Scene(root, 600, 400));
        stage.getScene().getStylesheets().add("/com/example/proiect/stylesheet.css");
        stage.show();
    }
}


