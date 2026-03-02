package com.dorustree.dorustree_corp.Repository.MySql;

import com.dorustree.dorustree_corp.Enums.ProductDeleteStatus;
import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.dorustree.dorustree_corp.Model.MySql.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ===============================
    // FIND BY CATEGORY
    // ===============================
    Page<Product> findAllByProductCategory(String productCategory, Pageable pageable);

    // ===============================
    // FIND BY STATUS
    // ===============================
    Page<Product> findAllByProductStatus(ProductStatus productStatus, Pageable pageable);

    // ===============================
    // FIND BY VENDOR
    // ===============================
    Page<Product> findAllByProductVendorId(String productVendorId, Pageable pageable);

    // ===============================
    // SEARCH WITH STATUS
    // ===============================
    Page<Product> findByProductStatusAndProductNameContainingIgnoreCaseOrProductStatusAndProductCategoryContainingIgnoreCase(
            ProductStatus status1, String name,
            ProductStatus status2, String category,
            Pageable pageable
    );

    Page<Product> getAllByProductVendorId(PageRequest of, String vendorId);

    Page<Product> findAllByProductVendorIdAndProductDeleteStatus(
            String vendorId,
            ProductDeleteStatus productDeleteStatus,
            Pageable pageable
    );

    // Count total products for a vendor

    @Query("SELECT COUNT(p) FROM Product p WHERE p.productVendorId = :vendorId AND p.productDeleteStatus = :status")
    Long countByVendorIdAndStatus(String vendorId, ProductDeleteStatus status);

    long countByProductDeleteStatus(ProductDeleteStatus deleteStatus);

    long countByProductStatusAndProductDeleteStatus(
            ProductStatus productStatus,
            ProductDeleteStatus deleteStatus
    );// ACTIVE / INACTIVE
}
