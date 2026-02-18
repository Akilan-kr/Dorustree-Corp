package com.example.dorustree_corp.Service.Interfaces;

import com.example.dorustree_corp.Enums.UserRoles;
import com.example.dorustree_corp.Enums.UserStatusForVendor;
import com.example.dorustree_corp.Model.MongoDb.UserData;
import com.example.dorustree_corp.Model.MySql.BlacklistToken;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface UserService {
    String findByUserId();
    void addUser(UserData userData);

    List<UserData> getAllUsers();

    UserData getUserById(String id);

    void updateUser(UserData userData);


    List<UserData> getAllUsersByRole(UserRoles userrole);

    void requestToBecameVendor();

    UserRoles findUserRole(@NotBlank String userName);

    List<UserData> getAllRequestDetails(UserStatusForVendor userstatusforvendor);

    void promoteUserToVendor(String userid, UserStatusForVendor userStatusForVendor);

    void logout(BlacklistToken blacklistedToken);
}
