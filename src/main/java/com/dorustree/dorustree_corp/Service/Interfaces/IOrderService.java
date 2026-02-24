package com.dorustree.dorustree_corp.Service.Interfaces;

import com.dorustree.dorustree_corp.Enums.OrderStatus;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderData;

import java.util.List;

public interface IOrderService {
    void placeOrder(OrderData orderData);

    OrderData getOrderOfLoginUser();

    boolean updateOrderStatus(OrderData orderData, OrderStatus orderstatus);

    List<OrderData> getAllOrders();

    List<OrderData> getAllOrdersByVendorId(String vendorid);

    List<OrderData> getAllOrderByOrderStatus(OrderStatus orderstatus);
}
