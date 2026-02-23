package com.dorustree.dorustree_corp.Service.Interfaces;

import com.dorustree.dorustree_corp.Dto.ProductRequest;
import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.dorustree.dorustree_corp.Model.MySql.Product;

import java.util.List;

public interface IProductService {
    void addProduct(ProductRequest productRequest);

    Product getProductById(Long id);

    List<Product> getAllProducts(int page, int size, String search);


    List<Product> getAllProductsByCategory(String productCategory);

    void updateProduct(Product product);

    void deleteProductById(Long id);


    List<Product> getAllProductsUsingVendorId(int page, int size, String productvendorid);

    List<Product> getAllProductForLoginVendor(int page, int size);

    Integer getProductPrice(String id);

    void updateStatusOfTheProduct(String productid);

    List<Product> getAllProductsByStatus(int page, int size,ProductStatus productstatus);
}

