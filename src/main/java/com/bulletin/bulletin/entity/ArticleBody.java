package com.bulletin.bulletin.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ArticleBody {

    @Id
    private UUID id;

    @Column(length = 10000)
    private String body;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Article article;

    public String getBody() {
        return body;
    }

    public ArticleBody() {

    }

    public ArticleBody(String body, Article article) {
        this.body = body;
        this.article = article;
    }
}
