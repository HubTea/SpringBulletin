package com.bulletin.bulletin.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    public String title;

    public Integer commentVersion;

    public LocalDateTime createdAt;
}
