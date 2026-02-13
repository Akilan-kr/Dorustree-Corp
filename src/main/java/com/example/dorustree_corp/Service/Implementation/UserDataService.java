package com.example.dorustree_corp.Service.Implementation;

import com.example.dorustree_corp.Model.MongoDb.UserData;
import com.example.dorustree_corp.Repository.MongoDb.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserDataService implements UserDetailsService {



    private final UserRepository userRepository;

    @Autowired
    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String userEmail) throws UsernameNotFoundException {
        Optional<UserData> existingUser =  userRepository.findByUserEmail(userEmail);
        if(existingUser.isEmpty())
            throw new UsernameNotFoundException("User not found with email: " + userEmail);

        UserData userData = existingUser.get();
        return new User(userData.getUserEmail(), userData.getUserPassword(), Collections.emptyList());//using empty collection list for now to solve the issue

    }
}
