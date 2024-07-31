package com.bulletin.bulletin.repository;

import com.bulletin.bulletin.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends
        ListCrudRepository<Comment, UUID>,
        ListPagingAndSortingRepository<Comment, UUID>
{
    @Query("""
            select comment
            from Comment comment
            where
                comment.article.id = :articleId and comment.parent.id is null and
                (comment.createdAt, comment.id) > (:cursorCreatedAt, :cursorId)
            order by comment.createdAt, comment.id
            limit :limit
    """)
    List<Comment> findByArticleId(UUID articleId, LocalDateTime cursorCreatedAt, UUID cursorId, Integer limit);

    @Query("""
            select comment
            from Comment comment
            where
                comment.article.id = :articleId and comment.parent.id = :parentId and
                (comment.createdAt, comment.id) > (:cursorCreatedAt, :cursorId)
            order by comment.createdAt, comment.id
            limit :limit
    """)
    List<Comment> findByArticleIdAndParentId(UUID articleId, UUID parentId, LocalDateTime cursorCreatedAt, UUID cursorId, Integer limit);
}
