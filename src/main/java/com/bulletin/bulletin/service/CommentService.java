package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.Comment;
import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.CommentRepository;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    UserRepository userRepository;

    CommentRepository commentRepository;

    UserService userService;

    CommentService(UserRepository userRepository, CommentRepository commentRepository, UserService userService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public void create(String writerId, String body) throws Exception {
        User writer = userService.findExistUser(writerId);
        Comment comment = new Comment(body, writer);

        commentRepository.save(comment);
    }
}
