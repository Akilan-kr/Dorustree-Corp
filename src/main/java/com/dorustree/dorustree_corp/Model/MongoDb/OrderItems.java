package com.dorustree.dorustree_corp.Model.MongoDb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItems {
    private String productVendorId;
    private String productId;
    private Integer productQuantity;
    private Integer productPrice;
}
