package com.bulletin.bulletin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private Integer commentVersion;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getCommentVersion() {
        return commentVersion;
    }

    public void setCommentVersion(Integer x) {
        commentVersion = x;
    }

    public Article() {

    }

    public Article(String title, Integer commentVersion, User writer) {
        this.title = title;
        this.commentVersion = commentVersion;
        this.writer = writer;
    }
}
