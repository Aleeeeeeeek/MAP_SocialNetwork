package com.example.domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable {
    private static final long serialVersionUID = 7331115341259248461L;
    protected ID id;

    public Entity() {
    }

    public ID getId() {
        return this.id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
