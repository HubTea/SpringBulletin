package com.bulletin.bulletin.service;

import java.util.List;

public class Page<T> {

    public List<T> content;

    public Boolean isLast;

    Page(List<T> content, Boolean isLast) {
        this.content = content;
        this.isLast = isLast;
    }
}
