package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Model.Product;
import com.example.dorustree_corp.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productServiceImplementation;

    @Autowired
    public ProductController(ProductService productServiceImplementation) {
        this.productServiceImplementation = productServiceImplementation;
    }

    @PostMapping("/addproduct")
    public String addProduct(@RequestBody Product product){
        productServiceImplementation.addProduct(product);
        return "Added";
    }
}
