package com.example.repository.db;

import com.example.domain.FriendRequest;
import com.example.domain.validators.Validator;
import com.example.repository.memory.InMemoryRepository;


import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class FriendRequestRepository extends InMemoryRepository<Long, FriendRequest> {
    private final String url;
    private final String username;
    private final String password;

    public FriendRequestRepository(Validator<FriendRequest> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public FriendRequest findOne(Long aLong) {
        String sql = "SELECT * from friend_requests where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id1 = resultSet.getLong("id1");
            Long id2 = resultSet.getLong("id2");
            String status = resultSet.getString("status");
            String data = resultSet.getString("date");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(data, formatter);

            FriendRequest friendRequest = new FriendRequest(id1, id2, status,dateTime);
            friendRequest.setId(aLong);
            return friendRequest;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        Set<FriendRequest> friendRequests = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friend_requests");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                friendRequests.add(findOne(id));
            }
            return friendRequests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest save(FriendRequest entity) {

        String sql = "insert into friend_requests (id1, id2, status,date) values (?, ?, ?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId1());
            ps.setLong(2, entity.getId2());
            ps.setString(3, entity.getStatus());
            ps.setString(4, entity.getDate().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest delete(Long aLong) {
        String sql = "DELETE from friend_requests WHERE id = ?";
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
