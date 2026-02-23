package com.dorustree.dorustree_corp.Repository.MongoDb;

import com.dorustree.dorustree_corp.Model.MongoDb.CartData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<CartData, String> {


    void deleteByUserId(String loggedInUserId);

    Optional<CartData> findByUserId(String loggedInUserId);
}
