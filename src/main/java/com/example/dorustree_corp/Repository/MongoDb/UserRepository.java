package com.example.dorustree_corp.Repository.MongoDb;

import com.example.dorustree_corp.Enums.UserRoles;
import com.example.dorustree_corp.Enums.UserStatusForVendor;
import com.example.dorustree_corp.Model.MongoDb.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserData, String> {

    Optional<UserData> findByUserEmail(String email);

    List<UserData> findAllByUserRole(UserRoles userrole);

    List<UserData> findAllByUserStatusForVendor(UserStatusForVendor userstatusforvendor);

}
