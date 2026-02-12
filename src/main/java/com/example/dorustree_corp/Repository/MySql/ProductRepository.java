package com.example.dorustree_corp.Repository.MySql;

import com.example.dorustree_corp.Model.MySql.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByProductCategory(String productcategory);

    List<Product> findAllByProductStatus(Boolean productstatus);

}
