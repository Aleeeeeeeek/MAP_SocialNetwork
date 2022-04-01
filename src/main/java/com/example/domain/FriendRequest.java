package com.example.domain;

import java.time.LocalDate;
import java.util.Objects;

public class FriendRequest extends Entity<Long> {
    private Long id1;
    private Long id2;
    private String status;
    private LocalDate date;

    public FriendRequest(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2) && Objects.equals(status, that.status) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, status, date);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }


    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public FriendRequest(Long id1, Long id2, String status, LocalDate date) {
        this.id1 = id1;
        this.id2 = id2;
        this.status = status;
        this.date = date;
    }
}
