package com.bulletin.bulletin.repository;

import com.bulletin.bulletin.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.UUID;

public interface CommentRepository extends
        ListCrudRepository<Comment, UUID>,
        ListPagingAndSortingRepository<Comment, UUID>
{
    Slice<Comment> findByArticleId(UUID articleId, Pageable pageable);
}
