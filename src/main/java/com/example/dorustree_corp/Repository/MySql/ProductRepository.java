package com.example.dorustree_corp.Repository.MySql;

import com.example.dorustree_corp.Enums.ProductStatus;
import com.example.dorustree_corp.Model.MySql.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByProductCategory(String productcategory);

    Page<Product> findAllByProductStatus(PageRequest pageRequest, ProductStatus productstatus);

    List<Product> getAllByProductVendorId(PageRequest pageRequest, String productvendorid);


}
