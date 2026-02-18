package com.example.dorustree_corp.Service.Implementation;

import com.example.dorustree_corp.Enums.UserRoles;
import com.example.dorustree_corp.Enums.UserStatusForVendor;
import com.example.dorustree_corp.Model.MongoDb.UserData;
import com.example.dorustree_corp.Model.MySql.BlacklistToken;
import com.example.dorustree_corp.Repository.MongoDb.UserRepository;
import com.example.dorustree_corp.Repository.MySql.BlacklistTokenRepository;
import com.example.dorustree_corp.Service.Interfaces.AuthenticationFacade;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationFacade authenticationFacade;

    private final BlacklistTokenRepository blacklistTokenRepository;
    @Autowired
    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder encoder, AuthenticationFacade authenticationFacade, BlacklistTokenRepository blacklistTokenRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authenticationFacade = authenticationFacade;
        this.blacklistTokenRepository = blacklistTokenRepository;
    }

    @Override
    public String findByUserId() {
        try {
            String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
            Optional<UserData> loggedInUser = userRepository.findByUserEmail(loggedInUserEmail);
            log.info("S: Get the User Data based on the login Id: {}", loggedInUserEmail);
            return loggedInUser.get().getId();
        }catch(Exception e) {
            throw new UsernameNotFoundException("User not founded", e);
        }
    }


    @Override
    public void addUser(UserData userData) {
        userData.setUserPassword(encoder.encode(userData.getUserPassword()));
        log.info("S: New user is register in db");
        userRepository.save(userData);
    }

    @Override
    public void logout(BlacklistToken blacklistedToken) {
        log.info("S: User logged out the token is save in a Blacklist");
        blacklistTokenRepository.save(blacklistedToken);
    }

    @Override
    public List<UserData> getAllUsers() {
        log.info("S: Get all the users");
        return userRepository.findAll();
    }

    @Override
    public UserData getUserById(String id) {
        Optional<UserData> userDataOptional = userRepository.findById(id);
        if(userDataOptional.isEmpty()){
            log.error("S: No user Founded with the Id:{}", id);
            throw new UsernameNotFoundException("no user founded");
        } else {
            log.info("S: Getting the user data for the Id: {}", id);
            return userDataOptional.get();
        }

    }

    @Override
    public void updateUser(UserData userData) {
        log.info("S: Update the user Data");
        userRepository.save(userData);
    }

    @Override
    public List<UserData> getAllUsersByRole(UserRoles userrole) {
        log.info("S: Getting all the User based on the Role({})", userrole);
        return userRepository.findAllByUserRole(userrole);
    }

    @Override
    public void requestToBecameVendor() {

        UserData userData = getUserById(findByUserId());
        userData.setUserStatusForVendor(UserStatusForVendor.Status_Pending);
        updateUser(userData);
    }

    @Override
    public List<UserData> getAllRequestDetails(UserStatusForVendor userstatusforvendor) {
        return userRepository.findAllByUserStatusForVendor(userstatusforvendor);
    }

    @Override
    public void promoteUserToVendor(String userid, UserStatusForVendor userStatusForVendor) {
        UserData userData = getUserById(userid);
        userData.setUserStatusForVendor(userStatusForVendor);
        if(userStatusForVendor == UserStatusForVendor.Status_Approved){
            userData.setUserRole(UserRoles.VENDOR);
        } else
            userData.setUserRole(UserRoles.USER);
        log.info("S: User({}) is promoted to Vendor", userid);
        updateUser(userData);
    }




}
