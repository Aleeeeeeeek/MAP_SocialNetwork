package com.example.controller;

import com.example.domain.*;
import com.example.domain.validators.FriendshipValidator;
import com.example.repository.db.FriendshipRepository;
import com.example.repository.memory.InMemoryRepository;
import com.example.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class FriendsController implements Initializable {

    @FXML
    private Button button_delete;

    @FXML
    private Button button_back;

    @FXML
    private Button button_message;

    @FXML
    private TableView<FriendWithDate> tv_friends;

    @FXML
    private TableColumn<FriendWithDate, String> col_first_name;

    @FXML
    private TableColumn<FriendWithDate, String> col_last_name;

    @FXML
    private TableColumn<FriendWithDate, String> col_username;

    @FXML
    private TableColumn<FriendWithDate, LocalDate> col_date;

    public static String clickedFriendUsername;
    public static String clickedFriendFirstName;
    public static String clickedFriendLastName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showFriends();
        button_back.setOnAction(event -> DBUtils.changeSceneProfile(event, "/com/example/proiect/profile.fxml", "Sisyphus-Network", LogInController.loggedUsername));
        button_delete.setOnAction(event -> {
            deleteFriendship();
            showFriends();
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Prietenia a fost stearsa!");
            confirmationAlert.show();
        });
        button_message.setOnAction(this::showMessages);
    }

    public ObservableList<FriendWithDate> getFriendsList() {
        ObservableList<FriendWithDate> friendsList = FXCollections.observableArrayList();
        InMemoryRepository<Long, Friendship> friendshipsRepo = new FriendshipRepository(new FriendshipValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        User currentUser = new User();
        Iterable<Friendship> friendships = friendshipsRepo.findAll();
        for (User user : LogInController.users) {
            if (Objects.equals(user.getUsername(), LogInController.loggedUsername)) {
                currentUser = user;
            }
        }
        for (Friendship friendship : friendships) {
            if (Objects.equals(friendship.getId1(), currentUser.getId())) {
                for (User user : LogInController.users) {
                    if (Objects.equals(user.getId(), friendship.getId2())) {
                        FriendWithDate friendWithDate = new FriendWithDate(friendship.getData(), user.getFirstName(), user.getLastName(),
                                user.getUsername(), friendship.getId());
                        friendsList.add(friendWithDate);
                    }
                }
            }
            if (Objects.equals(friendship.getId2(), currentUser.getId())) {
                for (User user : LogInController.users) {
                    if (Objects.equals(user.getId(), friendship.getId1())) {
                        FriendWithDate friendWithDate = new FriendWithDate(friendship.getData(), user.getFirstName(), user.getLastName(),
                                user.getUsername(), friendship.getId());
                        friendsList.add(friendWithDate);
                    }
                }
            }
        }
        return friendsList;
    }

    public void showFriends() {
        ObservableList<FriendWithDate> friends;
        friends = getFriendsList();
        col_first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_last_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("data"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        tv_friends.setItems(friends);
    }

    public void deleteFriendship() {
        InMemoryRepository<Long, Friendship> friendsRepo = new FriendshipRepository(new FriendshipValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        FriendWithDate user = tv_friends.getSelectionModel().getSelectedItem();
        if (user != null) {
            Friendship currentFriendship = new Friendship();
            for (Friendship friendship : friendsRepo.findAll()) {
                if (Objects.equals(friendship.getId(), user.getId())) {
                    currentFriendship = friendship;
                }
            }
            deleteFriend(currentFriendship.getId1(), currentFriendship.getId2());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu a fost selectat nici un prieten!");
            alert.show();
            throw new IllegalArgumentException("Nu a fost selectat nici un prieten!");
        }
    }

    public void deleteFriend(Long id1, Long id2) {
        InMemoryRepository<Long, Friendship> friendsRepo = new FriendshipRepository(new FriendshipValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        boolean ok = false;
        Iterable<Friendship> list = friendsRepo.findAll();
        if (Objects.equals(id1, id2)) {
            throw new IllegalArgumentException("Utilizatorii trebuie sa fie diferiti!");
        }
        for (Friendship friendship : list) {
            if ((Objects.equals(friendship.getId1(), id1) && Objects.equals(friendship.getId2(), id2))
                    || (Objects.equals(friendship.getId2(), id1) && Objects.equals(friendship.getId1(), id2))) {
                friendsRepo.delete(friendship.getId());
                ok = true;
            }
        }
        if (!ok) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Prietenia nu exista!");
            alert.show();
            throw new IllegalArgumentException("Prietenia nu exista!");
        }
    }

    public void showMessages(ActionEvent event) {
        FriendWithDate selectedUser = tv_friends.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            clickedFriendUsername = selectedUser.getUsername();
            clickedFriendFirstName = selectedUser.getFirstName();
            clickedFriendLastName = selectedUser.getLastName();
            DBUtils.changeSceneMessage(event, "/com/example/proiect/messages.fxml", "Sisyphus-Network", LogInController.loggedUsername);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu a fost selectat nici un prieten!");
            alert.show();
            throw new IllegalArgumentException("Nu a fost selectat nici un prieten!");
        }
    }
}
