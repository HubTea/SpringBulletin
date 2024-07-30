package com.bulletin.bulletin.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ArticleUserSession {

    @Embeddable
    public static class Id {

        public UUID articleId;

        public String userId;

        public Id() {

        }

        public Id(Article article, User user) {
            this.articleId = article.id;
            this.userId = user.id;
        }
    }

    @EmbeddedId
    public Id id;

    public int commentVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    @JoinColumn(name = "articleId", referencedColumnName = "id")
    public Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "userId", referencedColumnName = "id")
    public User user;

    public ArticleUserSession() {

    }

    public ArticleUserSession(Article article, User user, int commentVersion) {
        this.id = new Id(article, user);
        this.article = article;
        this.user = user;
        this.commentVersion = commentVersion;
    }
}
