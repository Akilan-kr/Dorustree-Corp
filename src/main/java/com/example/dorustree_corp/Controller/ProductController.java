package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Enums.ProductStatus;
import com.example.dorustree_corp.Model.MySql.Product;
import com.example.dorustree_corp.Service.ExcelService;
import com.example.dorustree_corp.Service.Interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {


    private final ProductService productServiceImplementation;
    private final ExcelService excelService;

    @Autowired
    public ProductController(ProductService productServiceImplementation, ExcelService excelService) {
        this.productServiceImplementation = productServiceImplementation;
        this.excelService = excelService;
    }

    @Operation(summary = "Add new product - VENDOR", description = "Returns a message product added")
    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping("/addproduct")
    public String addProduct(@Valid @RequestBody Product product){
        productServiceImplementation.addProduct(product);
//        if(product1 != null) {
        log.info("C: New Product added by the Vendor");
//            return product1;
//        }
        return "Added";
    }

    @Operation(summary = "Add new product in bulk using Excel - VENDOR", description = "Returns a ok status")
    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping("/upload-excel")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            log.info("C: Vendor try to upload the product data through Excel file");
            excelService.importProducts(file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.warn("C: Error while sending the product data using Excel file");
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Get product based on ProductId  - VENDOR", description = "Returns a Product data")
    @PreAuthorize("hasRole('VENDOR')")
    @GetMapping("/getproduct/{id}")
    public Product getProductById(@PathVariable Long id ){
        log.info("C: Get the products by its product id called by Vendor");
        return productServiceImplementation.getProductById(id);
    }

    @Operation(summary = "Get all Products - PUBLIC", description = "Returns a list of Products")
    @GetMapping("/getproducts")
    public List<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        log.info("C: Get Product is called by the user");
        return productServiceImplementation.getAllProducts(page, size);
    }

    @Operation(summary = "Get all Products based on VendorId - ADMIN", description = "Returns a list of Product based on the vendorId")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getproducts/{productvendorid}")
    public List<Product> getAllProductsUsingVendorId(@PathVariable String productvendorid,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
        log.info("C: Get products based on the product vendor is called Admin");
        return productServiceImplementation.getAllProductsUsingVendorId(page, size, productvendorid);
    }

    @Operation(summary = "Get all products of vendor who login - VENDOR", description = "Returns a list of Products based on the Vendor currently login")
    @PreAuthorize("hasRole('VENDOR')")
    @GetMapping("/getproductsofloginvendor")
    public List<Product> getAllProductForLoginVendor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        log.info("C: Get product based on login vendor is called");
        return productServiceImplementation.getAllProductForLoginVendor(page, size);
    }

    @Operation(summary = "Get all Product based on the product status - ADMIN, VENDOR", description = "Returns a list of product based on the status")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    @GetMapping("/getproductbystatus/{productstatus}")
    public List<Product> getAllProductsByStatus(@PathVariable ProductStatus productstatus,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size){
        log.info("C: Get product based on the product status is called");
        return productServiceImplementation.getAllProductsByStatus(page, size, productstatus);
    }

    @Operation(summary = "Get all product based on category - PUBLIC", description = "Returns a list of product based on the category")
    @GetMapping("/getproductsbycategory/{productcategory}")
    public List<Product> getAllProductsByCategory(@PathVariable String productcategory){
        log.info("C: Get product based on the product categories");
        return productServiceImplementation.getAllProductsByCategory(productcategory);
    }

    @Operation(summary = "Update the product by vendor - ADMIN, VENDOR", description = "Returns a message updated")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    @PutMapping("/updateproduct")
    public String updateProduct(@Valid @RequestBody Product product){
        log.info("C: Update the product detail by the vendor");
        productServiceImplementation.updateProduct(product);
        return "updated";
    }

    @Operation(summary = "Delete the product based on the ProductId - ADMIN, VENDOR", description = "Returns a message deleted")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    @DeleteMapping("/deleteproduct/{id}")
    public String deleteProduct(@PathVariable Long id){
        log.warn("C: Delete product by product id is called");
        productServiceImplementation.deleteProductById(id);
        return "deleted";
    }

    @Operation(summary = "Update the status of the product with it productId - VENDOR", description = "Returns a Updated message")
    @PreAuthorize("hasRole('VENDOR')")
    @PutMapping("/stutusofproduct/{productid}")
    public String updateStatusOfTheProduct(@PathVariable String productid){
        log.info("C: update the product Activeness for the product");
        productServiceImplementation.updateStatusOfTheProduct(productid);
        return "Updated";
    }


}
