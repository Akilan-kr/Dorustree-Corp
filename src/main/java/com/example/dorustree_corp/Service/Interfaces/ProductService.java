package com.example.dorustree_corp.Service.Interfaces;

import com.example.dorustree_corp.Enums.ProductStatus;
import com.example.dorustree_corp.Model.MySql.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);

    Product getProductById(Long id);

    Page<Product> getAllProducts(int page, int size);


    List<Product> getAllProductsByCategory(String productCategory);

    void updateProduct(Product product);

    void deleteProductById(Long id);


    List<Product> getAllProductsUsingVendorId(int page, int size, String productvendorid);

    List<Product> getAllProductForLoginVendor(int page, int size);

    Integer getProductPrice(String id);

    void updateStatusOfTheProduct(String productid);

    Page<Product> getAllProductsByStatus(int page, int size,ProductStatus productstatus);
}

