package com.example.dorustree_corp.Repository.MongoDb;

import com.example.dorustree_corp.Enums.OrderStatus;
import com.example.dorustree_corp.Model.MongoDb.OrderData;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<OrderData, Long> {
    OrderData findByOrderedUserId(String byUserId);

    List<OrderData> findByOrderedItemsProductVendorId(String vendorid);

    List<OrderData> findAllByOrderStatus(OrderStatus orderStatus);
}
