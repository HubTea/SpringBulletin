package com.bulletin.bulletin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.UUID;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    public String body;

    @CreationTimestamp
    public LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    public User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    public Article article;

    public Comment() {

    }

    public Comment(String body, User writer, Article article) {
        this.body = body;
        this.writer = writer;
        this.article = article;
    }
}
