package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User findExistUser(String id) throws Exception {
        Optional<User> optionalWriter = userRepository.findById(id);

        if(optionalWriter.isEmpty()) {
            throw new Exception();
        }
        return optionalWriter.get();
    }
}
