package com.example.domain;

import java.time.LocalDate;
import java.util.Objects;

public class FriendWithDate extends Entity<Long>{
    private LocalDate data;
    private String firstName;
    private String lastName;
    private String username;

    public FriendWithDate(){

    }

    public FriendWithDate(LocalDate data, String firstName, String lastName, String username, Long id) {
        this.data = data;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        setId(id);
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendWithDate that = (FriendWithDate) o;
        return Objects.equals(data, that.data) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, firstName, lastName, username);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
