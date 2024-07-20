package com.bulletin.bulletin.repository;

import com.bulletin.bulletin.entity.ArticleBody;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface ArticleBodyRepository extends ListCrudRepository<ArticleBody, UUID> {
}
