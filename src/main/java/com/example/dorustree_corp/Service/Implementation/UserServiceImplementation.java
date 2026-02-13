package com.example.dorustree_corp.Service.Implementation;

import com.example.dorustree_corp.Enums.UserRoles;
import com.example.dorustree_corp.Model.MongoDb.UserData;
import com.example.dorustree_corp.Repository.MongoDb.UserRepository;
import com.example.dorustree_corp.Service.Interfaces.AuthenticationFacade;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationFacade authenticationFacade;
    @Autowired
    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder encoder, AuthenticationFacade authenticationFacade) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public String findByUserId() {
        try {
            String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
            Optional<UserData> loggedInUser = userRepository.findByUserEmail(loggedInUserEmail);
            System.out.println(loggedInUser.get().getUserEmail());
            return loggedInUser.get().getId();
        }catch(Exception e) {
            throw new UsernameNotFoundException("User not founded", e);
        }
    }


    @Override
    public void addUser(UserData userData) {
        userData.setUserPassword(encoder.encode(userData.getUserPassword()));
        userRepository.save(userData);
    }

    @Override
    public List<UserData> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserData getUserById(String id) {
        Optional<UserData> userDataOptional = userRepository.findById(id);
        if(userDataOptional.isEmpty()){
            throw new UsernameNotFoundException("no user founder");
        } else {
            return userDataOptional.get();
        }

    }

    @Override
    public void updateUser(UserData userData) {
        userRepository.save(userData);
    }

    @Override
    public List<UserData> getAllUsersByRole(UserRoles userrole) {
        return userRepository.findAllByUserRole(userrole);
    }


}
