package com.example.dorustree_corp.Service.Interfaces;

import com.example.dorustree_corp.Model.MySql.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getAllProductsByCategory(String productCategory);

    void updateProduct(Product product);

    void deleteProductById(Long id);

    List<Product> getAllProductsByStatus(Boolean productstatus);

    List<Product> getAllProductsUsingVendorId(String productvendorid);

    List<Product> getAllProductForLoginVendor();

    Integer getProductPrice(String id);
}

