package com.example.dorustree_corp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItems {
    private String productVendorId;
    private String productId;
    private Integer productQuantity;
    private Integer productPrice;
}
