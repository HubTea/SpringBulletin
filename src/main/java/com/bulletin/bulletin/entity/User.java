package com.bulletin.bulletin.entity;

import jakarta.persistence.*;


@Entity
public class User {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public User() {

    }

    public User(String id) {
        this.id = id;
    }
}
