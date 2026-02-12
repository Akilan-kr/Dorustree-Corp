package com.example.dorustree_corp.Service;

import com.example.dorustree_corp.Model.MySql.Product;
import com.example.dorustree_corp.Repository.MySql.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService{

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImplementation(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(new Product());
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByCategory(String productCategory) {
        return productRepository.findAllByProductCategory(productCategory);
    }

    @Override
    public List<Product> getAllProductsByStatus(Boolean productstatus) {
        return productRepository.findAllByProductStatus(productstatus);
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }




}
