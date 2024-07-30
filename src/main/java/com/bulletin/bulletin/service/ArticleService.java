package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.entity.ArticleBody;
import com.bulletin.bulletin.entity.ArticleUserSession;
import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.ArticleBodyRepository;
import com.bulletin.bulletin.repository.ArticleRepository;
import com.bulletin.bulletin.repository.ArticleUserSessionRepository;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleService {

    public static class PageEntry {

        public Article article;

        public Boolean isUpdated;

        public Boolean isVisited;

        PageEntry(Article article, Boolean isUpdated, Boolean isVisited) {
            this.article = article;
            this.isUpdated = isUpdated;
            this.isVisited = isVisited;
        }
    }

    ArticleRepository articleRepository;

    ArticleBodyRepository articleBodyRepository;

    UserRepository userRepository;

    ArticleUserSessionRepository articleUserSessionRepository;

    UserService userService;

    ArticleUserSessionService articleUserSessionService;

    ArticleService(
            ArticleRepository articleRepository,
            ArticleBodyRepository articleBodyRepository,
            UserRepository userRepository,
            ArticleUserSessionRepository articleUserSessionRepository,
            UserService userService,
            ArticleUserSessionService articleUserSessionService
    ) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.articleBodyRepository = articleBodyRepository;
        this.articleUserSessionRepository = articleUserSessionRepository;
        this.userService = userService;
        this.articleUserSessionService = articleUserSessionService;
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

    public ArticleBody findExistBody(UUID id) throws Exception {
        Optional<ArticleBody> optionalBody = articleBodyRepository.findById(id);

        if(optionalBody.isEmpty()) {
            throw new Exception();
        }

        return optionalBody.get();
    }

    public List<PageEntry> getPage(Integer number, Integer size, String userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt", "id");
        Pageable pageable = PageRequest.of(number, size, sort);

        Page<Article> articleList = articleRepository.findAll(pageable);

        List<UUID> articleIdList = articleList.getContent().stream().map(article -> article.id).toList();

        List<ArticleUserSession> sessionList = articleUserSessionRepository.findByArticleIdAndUserId(articleIdList, userId);

        List<PageEntry> page = new ArrayList<>();
        boolean joined;
        boolean outdated;

        for(Article article: articleList.getContent()) {
            joined = false;
            outdated = false;

            for(ArticleUserSession session: sessionList) {
                if(article.id != session.id.article.id) {
                    continue;
                }

                joined = true;
                outdated = article.commentVersion > session.commentVersion;
                break;
            }
            page.add(new PageEntry(article, !outdated, joined));
        }

        return page;
    }

    public ArticleBody getBody(UUID id, String userId) throws Exception{
        Article article = findExistArticle(id);
        User user = userService.findExistUser(userId);
        Optional<ArticleUserSession> optionalSession = articleUserSessionRepository.findById(new ArticleUserSession.Id(article, user));

        if(optionalSession.isEmpty()) {
            articleUserSessionService.create(id, userId, article.commentVersion);
        }
        else {
            ArticleUserSession session = optionalSession.get();
            session.commentVersion = article.commentVersion;
        }

        return findExistBody(id);
    }
}
