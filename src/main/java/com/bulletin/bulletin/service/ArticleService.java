package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.entity.ArticleBody;
import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.ArticleBodyRepository;
import com.bulletin.bulletin.repository.ArticleRepository;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        articleBodyRepository.save(articleBody);
        articleRepository.save(article);
    }

    public Article findExistArticle(UUID id) throws Exception {
        Optional<Article> optionalArticle = articleRepository.findById(id);

        if(optionalArticle.isEmpty()) {
            throw new Exception();
        }
        return optionalArticle.get();
    }

    public List<Article> getPage(Integer number, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt", "id");
        Pageable pageable = PageRequest.of(number, size, sort);

        Page<Article> page = articleRepository.findAll(pageable);

        return page.getContent();
    }
}
