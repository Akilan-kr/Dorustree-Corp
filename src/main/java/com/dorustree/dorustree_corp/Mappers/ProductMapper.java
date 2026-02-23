package com.dorustree.dorustree_corp.Mappers;

import com.dorustree.dorustree_corp.Dto.ProductRequest;
import com.dorustree.dorustree_corp.Dto.ProductResponse;
import com.dorustree.dorustree_corp.Model.MySql.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // DTO → Entity
    public Product toEntity(ProductRequest request) {
        return Product.builder()
                .productName(request.productName())
                .productCategory(request.productCategory())
                .productPrice(request.productPrice())
                .productQuantity(
                        request.productQuantity() != null ? request.productQuantity() : 0
                )
                .build();
    }

    // Entity → DTO
    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getProductName(),
                product.getProductCategory(),
                product.getProductPrice(),
                product.getProductQuantity(),
                product.getProductStatus().name(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}

