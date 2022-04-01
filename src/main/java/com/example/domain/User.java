package com.example.domain;

import java.util.*;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String username;
    private final List<User> friends = new ArrayList<>();

    public User() {

    }

    public User(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public List<User> getFriends() {
        return this.friends;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String toString() {
        Set<String> stringOfFriends = new HashSet<>();
        for (User user : friends) {
            String ut = "{" + user.getId() + "; " + user.firstName + "; " + user.lastName + "} ";
            stringOfFriends.add(ut);
        }

        return "Utilizatorul{id: " + this.id + ", firstname: " + this.firstName + ", lastname: " + this.lastName + ", friends: " + stringOfFriends + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    public void makeFriend(User user) {
        this.friends.add(user);
    }
}

