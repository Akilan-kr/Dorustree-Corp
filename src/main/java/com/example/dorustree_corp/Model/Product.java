package com.example.dorustree_corp.Model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;
    @NotNull(message = "Product name cannot be null")
    private String productName;
    @NotNull(message = "Product category cannot be null")
    private String productCategory;
    @NotNull(message = "Product price cannot be null")
    private Integer productPrice;
    @NotNull(message = "Product Quantity cannot be null")
    private Integer productQuantity;
    private boolean productStatus;

    public Product(String productName, String productCategory, Integer productPrice, Integer productQuantity, boolean productStatus) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productStatus = productStatus;
    }
}
