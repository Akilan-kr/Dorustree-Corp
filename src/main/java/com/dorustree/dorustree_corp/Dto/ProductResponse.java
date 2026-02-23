package com.dorustree.dorustree_corp.Dto;

import java.time.LocalDateTime;

public record ProductResponse(

        Long productId,
        String productName,
        String productCategory,
        Integer productPrice,
        Integer productQuantity,
        String productStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}
