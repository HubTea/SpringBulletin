package com.bulletin.bulletin.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ArticleBody {
    @Id
    public UUID id;

    @Column(length = 10000)
    public String body;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Article article;

    public ArticleBody() {

    }

    public ArticleBody(String body, Article article) {
        this.body = body;
        this.article = article;
    }
}
