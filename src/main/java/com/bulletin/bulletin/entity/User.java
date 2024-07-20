package com.bulletin.bulletin.entity;

import jakarta.persistence.*;


@Entity
public class User {

    @Id
    public String id;

    public User() {

    }

    public User(String id) {
        this.id = id;
    }
}
