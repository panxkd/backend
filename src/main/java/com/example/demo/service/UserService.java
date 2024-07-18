package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userMapper.findById(id));
    }

    public void saveUser(User user) {
        if (user.getId() == null) {
            userMapper.insert(user);
        } else {
            userMapper.update(user);
        }
    }

    public void deleteUser(Long id) {
        userMapper.delete(id);
    }
}
