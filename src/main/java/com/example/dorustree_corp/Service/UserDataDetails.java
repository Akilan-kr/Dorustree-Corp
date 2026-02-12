package com.example.dorustree_corp.Service;

import com.example.dorustree_corp.Model.MongoDb.UserData;
import com.example.dorustree_corp.Repository.MongoDb.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Component
public class UserDataDetails implements UserDetailsService {



    private UserRepository userRepository;

    @Autowired
    public UserDataDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserData> existingUser =  userRepository.findByUserName(userName);
        if(existingUser.isEmpty())
            throw new UsernameNotFoundException("User not found with email: " + userName);

        UserData userData = existingUser.get();
        return new User(userData.getUserEmail(), userData.getUserPassword(), Collections.emptyList());//using empty collection list for now to solve the issue

    }
}
