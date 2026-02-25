package com.dorustree.dorustree_corp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorStatsDtoResponse {
    private Long totalProducts;
    private Integer totalSalesQuantity;
    private Integer totalSalesAmount;
}
