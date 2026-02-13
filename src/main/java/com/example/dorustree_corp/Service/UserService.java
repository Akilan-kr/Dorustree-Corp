package com.example.dorustree_corp.Service;

import com.example.dorustree_corp.Enums.UserRoles;
import com.example.dorustree_corp.Model.MongoDb.UserData;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    String findByUserId();
    void addUser(UserData userData);

    List<UserData> getAllUsers();

    UserData getUserById(String id);

    void updateUser(UserData userData);


    List<UserData> getAllUsersByRole(UserRoles userrole);
}
