package com.bulletin.bulletin.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ArticleUserSession {

    @Embeddable
    public static class Id {

        private UUID articleId;

        private String userId;

        public UUID getArticleId() {
            return articleId;
        }

        public Id() {

        }

        public Id(Article article, User user) {
            this.articleId = article.getId();
            this.userId = user.getId();
        }
    }

    @EmbeddedId
    private Id id;

    private int commentVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    @JoinColumn(name = "articleId", referencedColumnName = "id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public Id getId() {
        return id;
    }

    public int getCommentVersion() {
        return commentVersion;
    }

    public void setCommentVersion(int x) {
        commentVersion = x;
    }

    public ArticleUserSession() {

    }

    public ArticleUserSession(Article article, User user, int commentVersion) {
        this.id = new Id(article, user);
        this.article = article;
        this.user = user;
        this.commentVersion = commentVersion;
    }
}
