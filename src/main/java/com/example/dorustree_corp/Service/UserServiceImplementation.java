package com.example.dorustree_corp.Service;

import com.example.dorustree_corp.Model.MongoDb.UserData;
import com.example.dorustree_corp.Repository.MongoDb.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void addUser(UserData userData) {
         userRepository.save(userData);
    }

    @Override
    public List<UserData> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserData getUserById(String id) {
        return userRepository.findById(id).orElse(new UserData("id","name","email","password", "ADMIN","userAddress","123345"));
    }

    @Override
    public void updateUser(UserData userData) {
        userRepository.save(userData);
    }

}
