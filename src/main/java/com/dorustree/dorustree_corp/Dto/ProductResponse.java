package com.dorustree.dorustree_corp.Dto;

import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


public record ProductResponse(
        Long productId,
        String productName,
        String productCategory,
        Integer productPrice,
        Integer productQuantity,
        ProductStatus productStatus,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) implements Serializable {}

