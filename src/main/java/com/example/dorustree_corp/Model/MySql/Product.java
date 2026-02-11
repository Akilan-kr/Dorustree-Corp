package com.example.dorustree_corp.Model.MySql;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_sequence", initialValue = 1000, allocationSize = 1)
    private Long productId;
    @NotNull(message = "Product name cannot be null")
    private String productName;
    @NotNull(message = "Product category cannot be null")
    private String productCategory;
    @NotNull(message = "Product price cannot be null")
    private Integer productPrice;
    @NotNull(message = "Product Quantity cannot be null")
    private Integer productQuantity;
    private Boolean productStatus = false;

    public Product(String productName, String productCategory, Integer productPrice, Integer productQuantity, Boolean productStatus) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        if (productStatus != null && productQuantity != 0) {
            this.productStatus = productStatus;
        }
    }
}
