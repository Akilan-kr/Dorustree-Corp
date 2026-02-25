package com.dorustree.dorustree_corp.Service.Interfaces;

import com.dorustree.dorustree_corp.Dto.OrderRequest;
import com.dorustree.dorustree_corp.Dto.OrderResponse;
import com.dorustree.dorustree_corp.Enums.OrderStatus;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderData;

import java.util.List;

public interface IOrderService {
    void placeOrder(OrderRequest orderRequest);

    List<OrderData> getOrderOfLoginUser();

    boolean updateOrderStatus(OrderRequest orderRequest, OrderStatus orderstatus);

    List<OrderData> getAllOrders();

    List<OrderResponse> getAllOrdersByVendorId();

    List<OrderResponse> getAllOrderByOrderStatus(OrderStatus orderstatus);

    void updateOrderStatusById(String orderId, OrderStatus orderStatus);
}
