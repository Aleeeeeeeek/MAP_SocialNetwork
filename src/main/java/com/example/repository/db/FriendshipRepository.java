package com.example.repository.db;

import com.example.domain.Friendship;
import com.example.domain.validators.Validator;
import com.example.repository.memory.InMemoryRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class FriendshipRepository extends InMemoryRepository<Long, Friendship> {
    private final String url;
    private final String username;
    private final String password;

    public FriendshipRepository(Validator<Friendship> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship save(Friendship friendship) {

        String verifySql = "select count(*) from friendships f where f.id1 = ? and f.id2 = ? and f.data = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(verifySql)) {
            ps.setLong(1, friendship.getId2());
            ps.setLong(2, friendship.getId1());
            String dataString = friendship.getData().toString();
            ps.setString(3, dataString);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            int ok = resultSet.getInt(1);
            if (ok == 1) {
                throw new IllegalArgumentException("The friendship already exists!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        String sql = "insert into friendships (id1, id2, data) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, friendship.getId1());
            ps.setLong(2, friendship.getId2());
            String dataString = friendship.getData().toString();
            ps.setString(3, dataString);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long id) {
        String sql = "delete from friendships where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");

             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String data = resultSet.getString("data");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateTime = LocalDate.parse(data, formatter);

                Friendship friendship = new Friendship(id1, id2, dateTime);
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }
}
