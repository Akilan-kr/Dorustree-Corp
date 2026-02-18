package com.example.dorustree_corp.Service.Interfaces;

import com.example.dorustree_corp.Enums.OrderStatus;
import com.example.dorustree_corp.Model.MongoDb.OrderData;

import java.util.List;

public interface OrderService {
    void createOrder(OrderData orderData);

    OrderData getOrderOfLoginUser();

    boolean updateOrderStatus(OrderData orderData, OrderStatus orderstatus);

    List<OrderData> getAllOrders();

    List<OrderData> getAllOrdersByVendorId(String vendorid);

    List<OrderData> getAllOrderByOrderStatus(OrderStatus orderstatus);
}
