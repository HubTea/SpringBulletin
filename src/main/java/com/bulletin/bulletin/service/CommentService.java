package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.entity.Comment;
import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.CommentRepository;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Comment> getPage(Integer number, Integer size, UUID articleId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt", "id");
        Pageable pageable = PageRequest.of(number, size, sort);

        Slice<Comment> slice = commentRepository.findByArticleId(articleId, pageable);

        return slice.getContent();
    }
}
