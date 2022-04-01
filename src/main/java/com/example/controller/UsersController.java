package com.example.controller;

import com.example.domain.FriendRequest;
import com.example.domain.Friendship;
import com.example.domain.User;
import com.example.domain.validators.FriendRequestValidator;
import com.example.domain.validators.FriendshipValidator;
import com.example.repository.db.FriendRequestRepository;
import com.example.repository.db.FriendshipRepository;
import com.example.repository.memory.InMemoryRepository;
import com.example.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UsersController implements Initializable {

    ObservableList<User> modelGrade = FXCollections.observableArrayList();
    List<User> usersList = getUsersList();

    @FXML
    private Button button_log_out;

    @FXML
    private Label label_welcome;

    @FXML
    private TableView<User> tv_users;

    @FXML
    private TableColumn<User, String> col_first_name;

    @FXML
    private TableColumn<User, String> col_last_name;

    @FXML
    private TableColumn<User, String> col_username;

    @FXML
    private Button button_add;

    @FXML
    private Button button_withdraw;

    @FXML
    private TextField tf_search;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showUsers();
        button_log_out.setOnAction(event -> DBUtils.changeSceneProfile(event, "/com/example/proiect/profile.fxml", "Sisyphus-Network", LogInController.loggedUsername));
        button_add.setOnAction(event -> sendRequest());
        button_withdraw.setOnAction(event -> withdrawRequest());
    }

    private void handleFilter() {
        Predicate<User> p = user -> user.getUsername().startsWith(tf_search.getText());
        modelGrade.setAll(usersList
                .stream()
                .filter(p)
                .collect(Collectors.toList()));
    }

    public void setUserInformation(String username) {
        label_welcome.setText("Welcome " + username + "!");
    }

    public List<User> getUsersList() {
        List<User> usersList = new ArrayList<>();
        LogInController.users.forEach(usersList::add);
        return usersList;
    }

    public void showUsers() {
        col_first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_last_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        modelGrade.setAll(usersList);
        tv_users.setItems(modelGrade);
        tf_search.textProperty().addListener(o -> handleFilter());
    }

    public void sendRequest() {
        User currentUser = new User();
        for (User user : LogInController.users) {
            if (Objects.equals(user.getUsername(), LogInController.loggedUsername)) {
                currentUser = user;
            }
        }
        User clickedUser = new User();
        User selectedUser = tv_users.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            for (User user : LogInController.users) {
                if (Objects.equals(selectedUser.getId(), user.getId())) {
                    clickedUser = user;
                }
            }
            boolean ok = false;
            User textFieldUser = new User();
            for (User user : LogInController.users) {
                if (Objects.equals(user.getUsername(), clickedUser.getUsername())) {
                    textFieldUser = user;
                    ok = true;
                }
            }
            if (!ok) {
                System.out.println("Nu exista un utilizator cu acel username!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Nu exista un utilizator cu acel username!");
                alert.show();
                throw new IllegalArgumentException("Nu exista un utilizator cu acel username!");
            } else {
                if (!Objects.equals(currentUser.getId(), textFieldUser.getId())) {
                    saveRequest(currentUser.getId(), textFieldUser.getId(), "pending");
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setContentText("Cererea de prietenie a fost trimisa!");
                    confirmationAlert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Nu poti sa te imprietenesti cu tine insuti!");
                    alert.show();
                    throw new IllegalArgumentException("Nu poti sa te imprietenesti cu tine insuti!");
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu ai selectat nici un utilizator!");
            alert.show();
            throw new IllegalArgumentException("Nu ai selectat nici un utilizator!");
        }
    }

    public void saveRequest(Long id1, Long id2, String status) {
        InMemoryRepository<Long, FriendRequest> frRepo = new FriendRequestRepository(new FriendRequestValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        InMemoryRepository<Long, Friendship> friendsRepo = new FriendshipRepository(new FriendshipValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");

        Iterable<Friendship> friendships = friendsRepo.findAll();
        Iterable<FriendRequest> friendRequests = frRepo.findAll();
        for (Friendship friendship : friendships) {
            if ((Objects.equals(friendship.getId1(), id1) && Objects.equals(friendship.getId2(), id2))
                    || (Objects.equals(friendship.getId1(), id2) && Objects.equals(friendship.getId2(), id1))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Cererea nu se poate trimite! Prietenia exista deja!");
                alert.show();
                throw new IllegalArgumentException("Cererea nu se poate trimite! Prietenia exista deja!");
            }
        }
        for (FriendRequest friendRequest : friendRequests) {
            if ((Objects.equals(friendRequest.getId1(), id1) && Objects.equals(friendRequest.getId2(), id2))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Cererea nu se poate trimite! Ai trimis deja cerere!");
                alert.show();
                throw new IllegalArgumentException("Cererea nu se poate trimite! Ai trimis deja cerere!");
            }
            if ((Objects.equals(friendRequest.getId1(), id2) && Objects.equals(friendRequest.getId2(), id1))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ai primit deja o cerere de la acest utilizator!\nVerifica cererile de prietenie!");
                alert.show();
                throw new IllegalArgumentException("Ai primit deja o cerere de la acest utilizator!\nVerifica cererile de prietenie!");
            }
        }
        FriendRequest friendRequest = new FriendRequest(id1, id2, status, LocalDate.now());
        FriendRequestValidator validator = new FriendRequestValidator();
        validator.validate(friendRequest);
        frRepo.save(friendRequest);
    }

    private void withdrawRequest() {
        InMemoryRepository<Long, FriendRequest> frRepo = new FriendRequestRepository(new FriendRequestValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        Iterable<FriendRequest> friendRequests = frRepo.findAll();

        User selectedUser = tv_users.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            User currentUser = new User();
            FriendRequest selectedFriendRequest;

            for (User user : LogInController.users) {
                if (Objects.equals(user.getUsername(), LogInController.loggedUsername)) {
                    currentUser = user;
                }
            }
            boolean sent = false;
            for (FriendRequest friendRequest : friendRequests) {
                if (Objects.equals(currentUser.getId(), friendRequest.getId1()) && Objects.equals(selectedUser.getId(), friendRequest.getId2())) {
                    selectedFriendRequest = friendRequest;
                    FriendRequestController.deleteRequests(selectedFriendRequest.getId());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Cererea a fost retrasa!");
                    alert.show();
                    sent = true;
                }
            }
            if (!sent) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Nu se poate retrage cererea, deoarece nu a fost trimisa!");
                alert.show();
                throw new IllegalArgumentException("Nu se poate retrage cererea, deoarece nu a fost trimisa!");
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu ai selectat nici un utilizator!");
            alert.show();
            throw new IllegalArgumentException("Nu ai selectat nici un utilizator!");
        }
    }
}
