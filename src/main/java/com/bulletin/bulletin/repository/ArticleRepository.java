package com.bulletin.bulletin.repository;


import com.bulletin.bulletin.entity.Article;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.UUID;

public interface ArticleRepository extends ListCrudRepository<Article, UUID>, ListPagingAndSortingRepository<Article, UUID> {
}
