package com.bulletin.bulletin.entity;

import jakarta.persistence.*;

import java.util.UUID;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    public String body;

    public LocalDateTime createdAt;
}
