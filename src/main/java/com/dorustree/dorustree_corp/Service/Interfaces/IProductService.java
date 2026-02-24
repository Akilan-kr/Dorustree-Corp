package com.dorustree.dorustree_corp.Service.Interfaces;

import com.dorustree.dorustree_corp.Dto.ProductRequest;
import com.dorustree.dorustree_corp.Dto.ProductResponse;
import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.dorustree.dorustree_corp.Model.MySql.Product;

import java.util.List;

public interface IProductService {
    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getAllProducts(int page, int size, String search);


    List<ProductResponse> getAllProductsByCategory(String productCategory, int page, int size);

    ProductResponse updateProduct(Long id,ProductRequest productRequest);

    void deleteProductById(Long id);


    List<ProductResponse> getAllProductsUsingVendorId(int page, int size, String productvendorid);

    List<ProductResponse> getAllProductForLoginVendor(int page, int size);

//    Integer getProductPrice(String id);

    void updateStatusOfTheProduct(String productid);

    List<ProductResponse> getAllProductsByStatus(int page, int size,ProductStatus productstatus);
}

