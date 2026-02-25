package com.dorustree.dorustree_corp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderItemsDto {
    private String productId;
    private Integer productQuantity;
    private Integer productPrice;
}