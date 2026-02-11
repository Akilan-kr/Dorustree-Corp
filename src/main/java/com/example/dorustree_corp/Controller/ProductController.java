package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Model.MySql.Product;
import com.example.dorustree_corp.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getproduct/{id}")
    public Product getProductById(@PathVariable Long id ){
        return productServiceImplementation.getProductById(id);
    }

    @GetMapping("/getproducts")
    public List<Product> getAllProducts(){
        return productServiceImplementation.getAllProducts();
    }

    @GetMapping("/getproducts/{productCategory}")
    public List<Product> getAllProductsByCategory(@PathVariable String productCategory){
        return productServiceImplementation.getAllProductsByCategory(productCategory);
    }

    @PutMapping("/updateproduct")
    public String updateProduct(@RequestBody Product product){
        productServiceImplementation.updateProduct(product);
        return "updated";
    }

    @DeleteMapping("/deleteproduct/{id}")
    public String deleteProduct(@PathVariable Long id){
        productServiceImplementation.deleteProductById(id);
        return "deleted";
    }

}
