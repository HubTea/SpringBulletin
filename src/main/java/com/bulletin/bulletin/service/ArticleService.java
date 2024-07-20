package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.entity.ArticleBody;
import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.ArticleBodyRepository;
import com.bulletin.bulletin.repository.ArticleRepository;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {

    ArticleRepository articleRepository;

    ArticleBodyRepository articleBodyRepository;

    UserRepository userRepository;

    ArticleService(ArticleRepository articleRepository, ArticleBodyRepository articleBodyRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.articleBodyRepository = articleBodyRepository;
    }

    public void create(String writerId, String title, String body) throws Exception {
        Optional<User> optionalWriter = userRepository.findById(writerId);

        if(optionalWriter.isEmpty()) {
            throw new Exception();
        }

        Article article = new Article(title, 0, optionalWriter.get());
        ArticleBody articleBody = new ArticleBody(body, article);

        articleRepository.save(article);
        articleBodyRepository.save(articleBody);
    }
}
