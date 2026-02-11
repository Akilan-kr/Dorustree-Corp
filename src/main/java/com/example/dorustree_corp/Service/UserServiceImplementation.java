package com.example.dorustree_corp.Service;

import com.example.dorustree_corp.Model.MongoDb.User;
import com.example.dorustree_corp.Repository.MongoDb.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void addUser(User user) {
         userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findByUserId(id).orElse(new User(1L,"name","email","password","role","userAddress","123345"));
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
