package com.bulletin.bulletin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    public String title;

    public Integer commentVersion;

    @CreationTimestamp
    public LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    public User writer;

    public Article() {

    }

    public Article(String title, Integer commentVersion, User writer) {
        this.title = title;
        this.commentVersion = commentVersion;
        this.writer = writer;
    }
}
