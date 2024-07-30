package com.bulletin.bulletin.entity;

import jakarta.persistence.*;

@Entity
public class ArticleUserSession {

    public static class Id {
        @ManyToOne(fetch = FetchType.LAZY)
        public Article article;

        @ManyToOne(fetch = FetchType.LAZY)
        public User user;

        public Id() {

        }

        public Id(Article article, User user) {
            this.article = article;
            this.user = user;
        }
    }

    @EmbeddedId
    public Id id;

    public int commentVersion;

    public ArticleUserSession() {

    }

    public ArticleUserSession(Article article, User user, int commentVersion) {
        this.id = new Id(article, user);
        this.commentVersion = commentVersion;
    }
}
