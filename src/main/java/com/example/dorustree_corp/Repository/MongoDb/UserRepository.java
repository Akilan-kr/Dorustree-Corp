package com.example.dorustree_corp.Repository.MongoDb;

import com.example.dorustree_corp.Model.MongoDb.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserData, String> {

    Optional<UserData> findByUserEmail(String email);

    Optional<UserData> findByUserName(String userName);
}
