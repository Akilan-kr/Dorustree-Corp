package com.dorustree.dorustree_corp.Dto;



import com.dorustree.dorustree_corp.Enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductRequest(

        @NotBlank(message = "Product name cannot be empty")
        String productName,

        @NotBlank(message = "Product category cannot be empty")
        String productCategory,

        @Min(value = 1, message = "Price must be greater than zero")
        Integer productPrice,

        Integer productQuantity,

        ProductStatus productStatus

) {}
