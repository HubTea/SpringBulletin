package com.bulletin.bulletin.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ArticleBody {
    @Id
    public UUID id;

    @Column(length = 10000)
    private String body;
}
