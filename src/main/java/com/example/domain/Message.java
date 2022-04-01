package com.example.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long> {
    private Long from_user;
    private Long to_user;
    private String message;
    private LocalDateTime data;
    private Long reply;

    public Message() {
        this.id = 0L;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    public Long getFrom() {
        return from_user;
    }

    public Long getTo() {
        return to_user;
    }

    public void setFrom(Long from) {
        this.from_user = from;
    }

    public void setTo(Long to) {
        this.to_user = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Message(Long from, Long to, String message, LocalDateTime data, Long reply) {
        this.from_user = from;
        this.to_user = to;
        this.message = message;
        this.data = data;
        this.reply = reply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(id, message1.id) && Objects.equals(from_user, message1.from_user) && Objects.equals(to_user, message1.to_user) && Objects.equals(message, message1.message) && Objects.equals(data, message1.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from_user, to_user, message, data);
    }
}
