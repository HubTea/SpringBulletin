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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ArticleService {

    public static class PageEntry {

        public Article article;

        //isVisited가 false인 경우 isUpdated는 아무 의미가 없다.
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

    @Transactional
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

        List<UUID> articleIdList = articleList.getContent().stream().map(Article::getId).toList();

        List<ArticleUserSession> sessionList = articleUserSessionRepository.findByArticleIdAndUserId(articleIdList, userId);

        List<PageEntry> page = new ArrayList<>();
        boolean joined;
        boolean updated;

        for(Article article: articleList.getContent()) {
            joined = false;
            updated = false;

            for(ArticleUserSession session: sessionList) {
                if(!article.getId().equals(session.getId().getArticleId())) {
                    continue;
                }

                joined = true;
                updated = article.getCommentVersion() > session.getCommentVersion();
                break;
            }
            page.add(new PageEntry(article, updated, joined));
        }

        return page;
    }

    @Transactional
    public ArticleBody getBody(UUID id, String userId) throws Exception{
        Article article = findExistArticle(id);
        User user = userService.findExistUser(userId);
        Optional<ArticleUserSession> optionalSession = articleUserSessionRepository.findById(new ArticleUserSession.Id(article, user));

        if(optionalSession.isEmpty()) {
            articleUserSessionService.create(id, userId, article.getCommentVersion());
        }
        else {
            ArticleUserSession session = optionalSession.get();
            session.setCommentVersion(article.getCommentVersion());
        }

        return findExistBody(id);
    }
}
