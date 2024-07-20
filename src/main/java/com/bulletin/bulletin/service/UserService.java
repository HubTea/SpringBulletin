package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(String id) {
        User user = new User(id);

        userRepository.save(user);
    }
}
