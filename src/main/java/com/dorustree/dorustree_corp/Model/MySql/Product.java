package com.dorustree.dorustree_corp.Model.MySql;

import com.dorustree.dorustree_corp.Enums.ProductDeleteStatus;
import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.dorustree.dorustree_corp.Exceptions.ProductValidationException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_sequence", initialValue = 1000, allocationSize = 1)
    private Long productId;
    @NotBlank(message = "Product name cannot be null or empty")
    private String productName;
    @NotBlank(message = "Product category cannot be null or empty")
    private String productCategory;
    private Integer productPrice;
    private Integer productQuantity = 0;
    private ProductStatus productStatus = ProductStatus.INACTIVE;
    private ProductDeleteStatus productDeleteStatus = ProductDeleteStatus.NOT_DELETED;
    private String productVendorId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Product(String productName, String productCategory, Integer productPrice, Integer productQuantity, ProductStatus productStatus) {
        this.productName = productName;
        this.productCategory = productCategory;
        if(productPrice > 0)
            this.productPrice = productPrice;
        else
            throw new ProductValidationException("Price must be greater than zero");
        if(productQuantity > 0)
            this.productQuantity = productQuantity;
        if (productStatus != null && productQuantity != 0)
            this.productStatus = productStatus;
    }

}
