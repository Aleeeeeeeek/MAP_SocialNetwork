package com.example.domain;

import java.time.LocalDate;
import java.util.Objects;

public class UserWithFRDate extends Entity<Long> {

    private String username;
    private LocalDate date;

    public UserWithFRDate(String username, LocalDate date, Long id){
        this.username = username;
        this.date = date;
        setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserWithFRDate that = (UserWithFRDate) o;
        return Objects.equals(username, that.username) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, date);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
