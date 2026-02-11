package com.example.dorustree_corp.Repository.MongoDb;

import com.example.dorustree_corp.Model.MongoDb.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findByUserId(Long id);

}
