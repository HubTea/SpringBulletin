package com.bulletin.bulletin.repository;

import com.bulletin.bulletin.entity.Comment;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface CommentRepository extends ListCrudRepository<Comment, UUID> {
}
