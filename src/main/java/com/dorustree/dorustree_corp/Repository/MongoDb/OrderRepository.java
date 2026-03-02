package com.dorustree.dorustree_corp.Repository.MongoDb;

import com.dorustree.dorustree_corp.Enums.OrderStatus;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<OrderData, String> {
    List<OrderData> findByOrderedUserId(String byUserId);

    List<OrderData> findByOrderedItemsProductVendorId(String vendorid);

    List<OrderData> findAllByOrderStatus(OrderStatus orderStatus);

    // Fetch all orders for this vendor where order status = ORDER_RECEIVED
    @Query("{'orderedItems.productVendorId': ?0, 'orderStatus': ?1}")
    List<OrderData> findByVendorAndStatus(String loggedInUser, OrderStatus orderStatus);

    long countByOrderStatus(OrderStatus orderStatus); // "Order_Received", etc.
    List<OrderData> findByOrderStatusAndOrderDateBetween(OrderStatus orderStatus, Instant orderDate, Instant orderDate2);

}
