package com.example.repository.db;

import com.example.domain.User;
import com.example.domain.validators.Validator;
import com.example.repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserRepository extends InMemoryRepository<Long, User> {
    private final String url;
    private final String username;
    private final String password;

    public UserRepository(Validator<User> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public User findOne(Long aLong) {
        String sql = "SELECT * from users u LEFT JOIN friendships f ON f.id2 = u.id or f.id1 = u.id WHERE u.id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            User user = new User();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");

                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUsername(username);
                user.setId(aLong);
                if (!Objects.equals(user.getId(), id1) && id1 != 0) {
                    User user1 = findOneDB(id1);
                    user.makeFriend(user1);
                }
                if (!Objects.equals(user.getId(), id2) && id2 != 0) {
                    User user2 = findOneDB(id2);
                    user.makeFriend(user2);
                }
            }

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findOneDB(Long aLong) {
        String sql = "SELECT * from users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String username = resultSet.getString("username");

            User user = new User(firstName, lastName, username);
            user.setId(aLong);

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                users.add(findOne(id));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public User save(User entity) {

        String sql = "insert into users (first_name, last_name, username) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getUsername());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public User delete(Long aLong) {
        String sql = "DELETE from users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, aLong);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User entity) {
        String sql = "UPDATE users SET (first_name, last_name) = (?, ?)  WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setLong(3, entity.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
