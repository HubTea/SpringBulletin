package com.bulletin.bulletin.repository;

import com.bulletin.bulletin.entity.User;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, String> {
}
