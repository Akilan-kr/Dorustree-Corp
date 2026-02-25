package com.dorustree.dorustree_corp.Service.Interfaces;

import com.dorustree.dorustree_corp.Dto.UserResponse;
import com.dorustree.dorustree_corp.Dto.UserRequest;
import com.dorustree.dorustree_corp.Dto.VendorStatsDtoResponse;
import com.dorustree.dorustree_corp.Enums.UserRoles;
import com.dorustree.dorustree_corp.Enums.UserStatusForVendor;
import com.dorustree.dorustree_corp.Model.MongoDb.UserData;
import com.dorustree.dorustree_corp.Model.MySql.BlacklistToken;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface IUserService {
    String findByUserId();
    void addUser(UserRequest userRequest);

    List<UserData> getAllUsers();

    UserData getUserById(String id);

    void updateUser(UserData userData);


    List<UserData> getAllUsersByRole(UserRoles userrole);

    void requestToBecameVendor();

    UserRoles findUserRole(@NotBlank String userName);

    List<UserData> getAllRequestDetails(UserStatusForVendor userstatusforvendor);

    void promoteUserToVendor(String userid, UserStatusForVendor userStatusForVendor);

    void logout(BlacklistToken blacklistedToken);

    UserResponse getUser();

    VendorStatsDtoResponse getVendorStats();
}
