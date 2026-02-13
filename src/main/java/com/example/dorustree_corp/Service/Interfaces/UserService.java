package com.example.dorustree_corp.Service.Interfaces;

import com.example.dorustree_corp.Enums.UserRoles;
import com.example.dorustree_corp.Model.MongoDb.UserData;

import java.util.List;

public interface UserService {
    String findByUserId();
    void addUser(UserData userData);

    List<UserData> getAllUsers();

    UserData getUserById(String id);

    void updateUser(UserData userData);


    List<UserData> getAllUsersByRole(UserRoles userrole);
}
