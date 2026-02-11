package com.example.dorustree_corp.Service;

import com.example.dorustree_corp.Model.MongoDb.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    void updateUser(User user);
}
