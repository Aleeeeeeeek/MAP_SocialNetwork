
package com.example.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private Long id1;
    private Long id2;
    private LocalDate data;

    public Friendship(){

    }

    public Friendship(Long id1, Long id2, LocalDate data) {
        this.id1 = id1;
        this.id2 = id2;
        this.data = data;
    }

    public Long getId1() {
        return this.id1;
    }

    public Long getId2() {
        return this.id2;
    }

    public LocalDate getData() {
        return this.data;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship friendship = (Friendship) o;
        return Objects.equals(id1, friendship.id1) && Objects.equals(id2, friendship.id2) && Objects.equals(data, friendship.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, data);
    }
}
