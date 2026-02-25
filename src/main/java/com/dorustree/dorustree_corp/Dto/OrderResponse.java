package com.dorustree.dorustree_corp.Dto;

import com.dorustree.dorustree_corp.Enums.OrderStatus;

import java.util.List;

public record OrderResponse(
        String id,
        String orderUserId,
        List<OrderItemsDto> orderedItems,
        Integer totalPrice,
        OrderStatus orderStatus
) {
}
