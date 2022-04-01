package com.example.repository.db;

import com.example.domain.Message;
import com.example.domain.validators.Validator;
import com.example.repository.memory.InMemoryRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class MessageRepository extends InMemoryRepository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;

    public MessageRepository(Validator<Message> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message findOne(Long aLong) {
        String sql = "SELECT * from messages  where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long from = resultSet.getLong("from_user");
            Long to = resultSet.getLong("to_user");
            String message = resultSet.getString("message");
            String data = resultSet.getString("data");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime dateTime = LocalDateTime.parse(data, formatter);

            Long reply = resultSet.getLong("reply");
            Message messageEntity = new Message(from, to, message, dateTime, reply);
            messageEntity.setId(aLong);
            return messageEntity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                messages.add(findOne(id));
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message save(Message entity) {

        String sql = "insert into messages (from_user, to_user, message, data, reply) values (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getFrom());
            ps.setLong(2, entity.getTo());
            String dataString = entity.getData().toString();
            String finalString = dataString.substring(0,23);
            ps.setString(3, entity.getMessage());
            ps.setString(4, finalString);
            ps.setLong(5, entity.getReply());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Message delete(Long aLong) {
        String sql = "DELETE from messages WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, aLong);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
