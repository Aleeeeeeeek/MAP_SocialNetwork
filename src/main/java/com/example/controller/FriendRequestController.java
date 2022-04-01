package com.example.controller;

import com.example.domain.FriendRequest;
import com.example.domain.Friendship;
import com.example.domain.UserWithFRDate;
import com.example.domain.User;
import com.example.domain.validators.FriendRequestValidator;
import com.example.domain.validators.FriendshipValidator;
import com.example.domain.validators.UserValidator;
import com.example.repository.db.FriendRequestRepository;
import com.example.repository.db.FriendshipRepository;
import com.example.repository.db.UserRepository;
import com.example.repository.memory.InMemoryRepository;
import com.example.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class FriendRequestController implements Initializable {

    @FXML
    private Button button_back;

    @FXML
    private Button button_accept;

    @FXML
    private Button button_decline;

    @FXML
    private TableColumn<UserWithFRDate, String> col_username;

    @FXML
    private TableColumn<UserWithFRDate, LocalDate> col_date;

    @FXML
    private TableView<UserWithFRDate> tv_friend_request;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showFriendRequests();
        button_back.setOnAction(event -> DBUtils.changeSceneProfile(event, "/com/example/proiect/profile.fxml", "Sisyphus-Network", LogInController.loggedUsername));
        button_accept.setOnAction(event -> {
            acceptRequest();
            showFriendRequests();
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Prietenia a fost realizata!");
            confirmationAlert.show();
        });
        button_decline.setOnAction(event -> {
            deleteRequest();
            showFriendRequests();
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Cererea a fost refuzata!");
            confirmationAlert.show();
        });
    }

    public ObservableList<UserWithFRDate> getFriendRequests() {
        ObservableList<UserWithFRDate> frList = FXCollections.observableArrayList();
        InMemoryRepository<Long, FriendRequest> frRepo = new FriendRequestRepository(new FriendRequestValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        User currentUser = new User();
        Iterable<FriendRequest> friendRequests = frRepo.findAll();
        for (User user : LogInController.users) {
            if (Objects.equals(user.getUsername(), LogInController.loggedUsername)) {
                currentUser = user;
            }
        }
        for (FriendRequest friendRequest : friendRequests) {
            if (Objects.equals(friendRequest.getId2(), currentUser.getId())) {
                for (User user : LogInController.users) {
                    if (Objects.equals(user.getId(), friendRequest.getId1())) {
                        UserWithFRDate userWithFRDate = new UserWithFRDate(user.getUsername(), friendRequest.getDate(), friendRequest.getId());
                        frList.add(userWithFRDate);
                    }
                }
            }
        }
        return frList;
    }

    public void showFriendRequests() {
        ObservableList<UserWithFRDate> friendRequests;
        friendRequests = getFriendRequests();
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        tv_friend_request.setItems(friendRequests);
    }

    public void acceptRequest() {
        UserWithFRDate user = tv_friend_request.getSelectionModel().getSelectedItem();
        if (user != null) {
            InMemoryRepository<Long, FriendRequest> frRepo = new FriendRequestRepository(new FriendRequestValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
            Iterable<FriendRequest> friendRequests = frRepo.findAll();
            FriendRequest currentFriendRequest = new FriendRequest();
            for (FriendRequest friendRequest : friendRequests) {
                if (Objects.equals(friendRequest.getId(), user.getId())) {
                    currentFriendRequest = friendRequest;
                }
            }
            deleteRequests(currentFriendRequest.getId());
            addFriend(currentFriendRequest.getId1(), currentFriendRequest.getId2(), LocalDate.now());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu ai selectat nici o cerere!");
            alert.show();
            throw new IllegalArgumentException("Nu ai selectat nici o cerere!");
        }
    }

    public void deleteRequest() {
        UserWithFRDate selectedFriendRequest = tv_friend_request.getSelectionModel().getSelectedItem();
        if (selectedFriendRequest != null) {
            InMemoryRepository<Long, FriendRequest> frRepo = new FriendRequestRepository(new FriendRequestValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
            Iterable<FriendRequest> friendRequests = frRepo.findAll();

            FriendRequest currentFriendRequest = new FriendRequest();
            for (FriendRequest friendRequest : friendRequests) {
                if (Objects.equals(friendRequest.getId(), selectedFriendRequest.getId())) {
                    currentFriendRequest = friendRequest;
                }
            }
            deleteRequests(currentFriendRequest.getId());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu ai selectat nici o cerere!");
            alert.show();
            throw new IllegalArgumentException("Nu ai selectat nici o cerere!");
        }
    }

    public static void deleteRequests(Long id) {
        InMemoryRepository<Long, FriendRequest> frRepo = new FriendRequestRepository(new FriendRequestValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        FriendRequest friendRequest = frRepo.findOne(id);
        FriendRequestValidator friendRequestValidator = new FriendRequestValidator();
        friendRequestValidator.validate(friendRequest);
        frRepo.delete(id);
    }

    public void addFriend(Long id1, Long id2, LocalDate data) {
        InMemoryRepository<Long, Friendship> friendsRepo = new FriendshipRepository(new FriendshipValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        InMemoryRepository<Long, User> usersRepo = new UserRepository(new UserValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        boolean ok = true;
        if (Objects.equals(id1, id2)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Un utilizator nu poate sa se imprieteneasca singur!");
            alert.show();
            throw new IllegalArgumentException("Un utilizator nu poate sa se imprieteneasca singur!");
        } else {
            User user1 = usersRepo.findOne(id1);
            User user2 = usersRepo.findOne(id2);
            UserValidator validator = new UserValidator();
            validator.validate(user1);
            validator.validate(user2);
            ok = isOk(id2, ok, user1);
            ok = isOk(id1, ok, user2);
            if (ok) {
                user1.makeFriend(user2);
                user2.makeFriend(user1);
                Friendship friendship = new Friendship(id1, id2, data);
                FriendshipValidator friendshipValidator = new FriendshipValidator();
                friendshipValidator.validate(friendship);
                friendsRepo.save(friendship);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Prietenia exista deja!");
                alert.show();
                throw new IllegalArgumentException("Prietenia exista deja!");
            }
        }
    }

    private boolean isOk(Long id, boolean ok, User utilizator) {
        List<User> list = utilizator.getFriends();
        for (User user : list) {
            if (Objects.equals(user.getId(), id)) {
                ok = false;
                break;
            }
        }
        return ok;
    }
}
