package com.bulletin.bulletin.repository;

import com.bulletin.bulletin.entity.ArticleUserSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;

public interface ArticleUserSessionRepository extends ListCrudRepository<ArticleUserSession, ArticleUserSession.Id> {

    @Query("select session from ArticleUserSession session where session.id.articleId in :articleIdList and session.id.userId = :userId")
    List<ArticleUserSession> findByArticleIdAndUserId(List<UUID> articleIdList, String userId);
}
