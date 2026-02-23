package com.dorustree.dorustree_corp.Repository.MySql;

import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.dorustree.dorustree_corp.Model.MySql.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByProductCategory(String productcategory);

    List<Product> findAllByProductStatus(PageRequest pageRequest, ProductStatus productstatus);

    List<Product> getAllByProductVendorId(PageRequest pageRequest, String productvendorid);


    Page<Product> findByProductStatusAndProductNameContainingIgnoreCaseOrProductStatusAndProductCategoryContainingIgnoreCase(
            ProductStatus status1, String name,
            ProductStatus status2, String category,
            Pageable pageable
    );

    List<Product> findByProductStatusAndProductNameContainingIgnoreCaseOrProductStatusAndProductCategoryContainingIgnoreCase(ProductStatus productStatus, String search, ProductStatus productStatus1, String search1);
}
