package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Model.MySql.Product;
import com.example.dorustree_corp.Service.Implementation.ExcelService;
import com.example.dorustree_corp.Service.Interfaces.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    Logger logger
            = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productServiceImplementation;
    private final ExcelService excelService;

    @Autowired
    public ProductController(ProductService productServiceImplementation, ExcelService excelService) {
        this.productServiceImplementation = productServiceImplementation;
        this.excelService = excelService;
    }

    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping("/addproduct")
    public String addProduct(@Valid @RequestBody Product product){
        productServiceImplementation.addProduct(product);
//        if(product1 != null) {
        logger.info("New Product added");
//            return product1;
//        }
        return "Added";
    }

    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping("/upload-excel")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            excelService.importProducts(file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/getproduct/{id}")
    public Product getProductById(@PathVariable Long id ){

        return productServiceImplementation.getProductById(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'VENDOR')")
    @GetMapping("/getproducts")
    public List<Product> getAllProducts(){

        return productServiceImplementation.getAllProducts();
    }

    @GetMapping("/getproducts/{productvendorid}")
    public List<Product> getAllProductsUsingVendorId(@PathVariable String productvendorid){
        return productServiceImplementation.getAllProductsUsingVendorId(productvendorid);
    }

    @GetMapping("/getproductsofloginvendor")
    public List<Product> getAllProductForLoginVendor(){
        return productServiceImplementation.getAllProductForLoginVendor();
    }

    @GetMapping("/getproductbystatus/{productstatus}")
    public List<Product> getAllProductsByStatus(@PathVariable Boolean productstatus){
        return productServiceImplementation.getAllProductsByStatus(productstatus);
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
