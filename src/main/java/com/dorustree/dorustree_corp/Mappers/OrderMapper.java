package com.dorustree.dorustree_corp.Mappers;

import com.dorustree.dorustree_corp.Dto.OrderItemsDto;
import com.dorustree.dorustree_corp.Dto.OrderRequest;
import com.dorustree.dorustree_corp.Dto.OrderResponse;
import com.dorustree.dorustree_corp.Enums.OrderStatus;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderData;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderItems;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderData toEntity(OrderRequest orderRequest, String userId) {
        List<OrderItems> items = orderRequest.getOrderedItems().stream()
                .map(req -> OrderItems.builder()
                        .productId(req.getProductId())
                        .productQuantity(req.getProductQuantity())
                        .build()
                ).toList();

        return OrderData.builder()
                .orderedUserId(userId)
                .orderedItems(items)
                .orderStatus(OrderStatus.Order_Initiated)
                .build();
    }

    public OrderResponse toResponse(OrderData order, String vendorId) {

        List<OrderItemsDto> vendorItems = order.getOrderedItems()
                .stream()
                .filter(item -> vendorId.equals(item.getProductVendorId()))
                .map(item -> new OrderItemsDto(
                        item.getProductId(),
                        item.getProductQuantity(),
                        item.getProductPrice()
                ))
                .toList();

        Integer vendorTotal = vendorItems.stream()
                .mapToInt(i -> i.getProductPrice() * i.getProductQuantity())
                .sum();

        return new OrderResponse(
                order.getId(),
                order.getOrderedUserId(),
                vendorItems,
                order.getOrderDate(),
                vendorTotal,
                order.getOrderStatus()
        );
    }
}
