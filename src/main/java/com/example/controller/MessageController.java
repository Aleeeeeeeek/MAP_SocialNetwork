package com.example.controller;

import com.example.domain.*;
import com.example.domain.validators.MessageValidator;
import com.example.repository.db.MessageRepository;
import com.example.repository.memory.InMemoryRepository;
import com.example.utils.DBUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class MessageController implements Initializable {

    @FXML
    private Button button_back;

    @FXML
    private Button button_send;

    @FXML
    private ScrollPane sp_main;

    @FXML
    private TextField tf_message;

    @FXML
    private VBox vbox_messages;

    @FXML
    private Label label_chat;

    @FXML
    private Button button_refresh;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showMessages();
        button_back.setOnAction(event -> DBUtils.changeSceneGeneric(event, "/com/example/proiect/friends.fxml", "Sisyphus-Network"));
        button_send.setOnAction(event -> {
            sendMessage();
            vbox_messages.getChildren().removeAll(vbox_messages.getChildren());
            showMessages();
        });
        vbox_messages.heightProperty().addListener((observable, oldValue, newValue) -> sp_main.setVvalue((Double) newValue));

        Image img = new Image("com/example/proiect/update.png");
        ImageView view = new ImageView(img);

        button_refresh.setGraphic(view);
        button_refresh.setOnAction(event -> {
            vbox_messages.getChildren().removeAll(vbox_messages.getChildren());
            showMessages();
        });

    }

    public void setText() {
        label_chat.setText(FriendsController.clickedFriendFirstName + " " + FriendsController.clickedFriendLastName);
    }

    public void showMessages() {
        List<Message> messages;
        User fromUser = new User();
        User toUser = new User();

        for (User user : LogInController.users) {
            if (Objects.equals(user.getUsername(), FriendsController.clickedFriendUsername)) {
                toUser = user;
            } else if (Objects.equals(user.getUsername(), LogInController.loggedUsername)) {
                fromUser = user;
            }
        }
        messages = showConversation(fromUser.getId(), toUser.getId());

        for (Message message : messages) {
            for (User user : LogInController.users) {
                if (Objects.equals(user.getId(), message.getFrom())) {
                    fromUser = user;
                } else if (Objects.equals(user.getId(), message.getTo())) {
                    toUser = user;
                }
            }
            if ((Objects.equals(fromUser.getUsername(), LogInController.loggedUsername) && Objects.equals(toUser.getUsername(), FriendsController.clickedFriendUsername))) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 10));
                Text text = new Text(message.getMessage());
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-color: white;" +
                        "-fx-background-color: linear-gradient(to bottom, #6bb7db, #3284ab);" +
                        "-fx-background-radius: 20px");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(0.934, 0.945, 0.996));
                hBox.getChildren().add(textFlow);
                vbox_messages.getChildren().add(hBox);
            } else if (Objects.equals(fromUser.getUsername(), FriendsController.clickedFriendUsername) && Objects.equals(toUser.getUsername(), LogInController.loggedUsername)) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(5, 5, 5, 10));
                Text text = new Text(message.getMessage());
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-color: #0368a7;" +
                        "-fx-background-color:  linear-gradient(to bottom, #e2f3fb, #caecfc);;" +
                        "-fx-background-radius: 20px");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(0.011, 0.407, 0.655));
                hBox.getChildren().add(textFlow);
                vbox_messages.getChildren().add(hBox);
            }
        }
    }

    public void sendMessage() {
        if (!tf_message.getText().isEmpty()) {
            //salvare mesaj in db
            User currentUser = new User();
            User toUser = new User();
            for (User user : LogInController.users) {
                if (Objects.equals(user.getUsername(), LogInController.loggedUsername)) {
                    currentUser = user;
                }
                if (Objects.equals(user.getUsername(), FriendsController.clickedFriendUsername)) {
                    toUser = user;
                }
            }
            saveMessage(currentUser.getId(), toUser.getId(), tf_message.getText(), LocalDateTime.now(), 0L);

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(tf_message.getText());
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: white;" +
                    "-fx-background-color: linear-gradient(to bottom, #6bb7db, #3284ab);" +
                    "-fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));
            hBox.getChildren().add(textFlow);
            vbox_messages.getChildren().add(hBox);
            tf_message.clear();
        }
    }

    public List<Message> showConversation(Long id1, Long id2) {
        InMemoryRepository<Long, Message> messagesRepo = new MessageRepository(new MessageValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        Iterable<Message> messages = messagesRepo.findAll();

        List<Message> conversation = new ArrayList<>();
        for (Message message : messages) {
            if ((Objects.equals(message.getFrom(), id1) && Objects.equals(message.getTo(), id2))
                    || (Objects.equals(message.getFrom(), id2) && Objects.equals(message.getTo(), id1))) {
                conversation.add(message);
            }
        }
        Comparator<Message> mapComparator = Comparator.comparing(Message::getData);
        conversation.sort(mapComparator);
        return conversation;
    }

    public void saveMessage(Long from_user, Long to_user, String message, LocalDateTime data, Long reply) {
        InMemoryRepository<Long, Message> messagesRepo = new MessageRepository(new MessageValidator(), "jdbc:postgresql://localhost:5432/academic", "postgres", "postgres");
        Message currentMessage = new Message(from_user, to_user, message, data, reply);
        MessageValidator messageValidator = new MessageValidator();
        messageValidator.validate(currentMessage);
        List<Message> messages = this.showConversation(from_user, to_user);
        Message lastMessage = new Message();
        int i = 0;
        for (Message mesaj : messages) {
            if (i == messages.size() - 1) {
                lastMessage = mesaj;
            }
            i++;
        }
        for (Message mesaj : messages) {
            if (Objects.equals(mesaj.getTo(), from_user) && Objects.equals(mesaj.getFrom(), to_user)
                    && currentMessage.getData().compareTo(mesaj.getData()) < 0) {
                throw new IllegalArgumentException("Mesajul adaugat trebuie sa fie cronologic dupa mesajul la care se raspunde!");
            }
        }
        if (Objects.equals(lastMessage.getTo(), currentMessage.getTo())
                && Objects.equals(lastMessage.getFrom(), currentMessage.getFrom())) {
            currentMessage.setReply(0L);
        } else {
            currentMessage.setReply(lastMessage.getId());
        }
        messagesRepo.save(currentMessage);
    }
}
