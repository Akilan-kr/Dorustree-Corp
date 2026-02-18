package com.example.dorustree_corp.Service.Interfaces;

import com.example.dorustree_corp.Enums.ProductStatus;
import com.example.dorustree_corp.Model.MySql.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);

    Product getProductById(Long id);

    List<Product> getAllProducts(int page, int size);

    List<Product> getAllProductsByCategory(String productCategory);

    void updateProduct(Product product);

    void deleteProductById(Long id);


    List<Product> getAllProductsUsingVendorId(int page, int size, String productvendorid);

    List<Product> getAllProductForLoginVendor(int page, int size);

    Integer getProductPrice(String id);

    void updateStatusOfTheProduct(String productid);

    List<Product> getAllProductsByStatus(int page, int size,ProductStatus productstatus);
}

