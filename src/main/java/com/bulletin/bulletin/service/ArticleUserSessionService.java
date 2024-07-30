package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.entity.ArticleUserSession;
import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.ArticleRepository;
import com.bulletin.bulletin.repository.ArticleUserSessionRepository;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleUserSessionService {

    ArticleUserSessionRepository articleUserSessionRepository;

    ArticleRepository articleRepository;

    UserRepository userRepository;

    ArticleUserSessionService(
            ArticleUserSessionRepository articleUserSessionRepository,
            ArticleRepository articleRepository,
            UserRepository userRepository) {
        this.articleUserSessionRepository = articleUserSessionRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public void create(UUID articleId, String userId, int commentVersion) throws Exception{
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalArticle.isEmpty() || optionalUser.isEmpty()) {
            //TODO: 다른 예외로 교체
            throw new Exception();
        }

        Article article = optionalArticle.get();
        User user = optionalUser.get();

        articleUserSessionRepository.save(new ArticleUserSession(article, user, commentVersion));
    }
}
