package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.entity.Comment;
import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.CommentRepository;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CommentService {

    UserRepository userRepository;

    CommentRepository commentRepository;

    UserService userService;

    ArticleService articleService;

    CommentService(
            UserRepository userRepository,
            CommentRepository commentRepository,
            UserService userService,
            ArticleService articleService
    ) {
        this.articleService = articleService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public void create(String body, String writerId, UUID articleId) throws Exception {
        User writer = userService.findExistUser(writerId);
        Article article = articleService.findExistArticle(articleId);
        Comment comment = new Comment(body, writer, article);

        commentRepository.save(comment);
    }
}
