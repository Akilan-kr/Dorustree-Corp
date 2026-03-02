package com.dorustree.dorustree_corp.Dto;

import com.dorustree.dorustree_corp.Enums.OrderStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        String id,
        String orderUserId,
        List<OrderItemsDto> orderedItems,
        Instant orderDate,
        Integer totalPrice,
        OrderStatus orderStatus
) {
}
