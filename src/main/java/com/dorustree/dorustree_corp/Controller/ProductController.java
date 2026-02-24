package com.dorustree.dorustree_corp.Controller;

import com.dorustree.dorustree_corp.Dto.ApiResponse;
import com.dorustree.dorustree_corp.Dto.ProductRequest;
import com.dorustree.dorustree_corp.Dto.ProductResponse;
import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.dorustree.dorustree_corp.Model.MySql.Product;
import com.dorustree.dorustree_corp.Service.ExcelService;
import com.dorustree.dorustree_corp.Service.Interfaces.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {


    private final IProductService productService;
    private final ExcelService excelService;

    @Autowired
    public ProductController(IProductService productService, ExcelService excelService) {
        this.productService = productService;
        this.excelService = excelService;
    }

    @Operation(summary = "Add new product - VENDOR", description = "Returns a message product added")
    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping("/addproduct")
    public ResponseEntity<ApiResponse<?>> addProduct(@Valid @RequestBody ProductRequest productRequest){
        log.info("C: New Product added by the Vendor");
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Product Added Successfully", productService.addProduct(productRequest)));
    }

    @Operation(summary = "Add new product in bulk using Excel - VENDOR", description = "Returns a ok status")
    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping("/upload-excel")
    public ResponseEntity<ApiResponse<?>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            log.info("C: Vendor try to upload the product data through Excel file");
            excelService.importProducts(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Product Added via Excel is done", null));
        } catch (Exception e) {
            log.warn("C: Error while sending the product data using Excel file");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Operation(summary = "Get product based on ProductId  - VENDOR", description = "Returns a Product data")
    @PreAuthorize("hasRole('VENDOR')")
    @GetMapping("/getproduct/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id ){
        log.info("C: Get the products by its product id called by Vendor");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Getting Product based on the Id", productService.getProductById(id)));
    }

    @Operation(summary = "Get all Products - PUBLIC", description = "Returns a list of Products")
    @GetMapping("/getproducts")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search){
        log.info("C: Get Product is called by the user");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Getting all the products from db using page, size and search",productService.getAllProducts(page, size, search)));
    }

    @Operation(summary = "Get all Products based on VendorId - ADMIN", description = "Returns a list of Product based on the vendorId")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getproducts/{productvendorid}")
    public ResponseEntity<List<ProductResponse>> getAllProductsUsingVendorId(@PathVariable String productvendorid,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
        log.info("C: Get products based on the product vendor is called Admin");
        return ResponseEntity.ok(productService.getAllProductsUsingVendorId(page, size, productvendorid));
    }

    @Operation(summary = "Get all products of vendor who login - VENDOR", description = "Returns a list of Products based on the Vendor currently login")
    @PreAuthorize("hasRole('VENDOR')")
    @GetMapping("/getproductsofloginvendor")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProductForLoginVendor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        log.info("C: Get product based on login vendor is called");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Getting Product Of a Login vendor", productService.getAllProductForLoginVendor(page, size)));
    }

    @Operation(summary = "Get all Product based on the product status - ADMIN, VENDOR", description = "Returns a list of product based on the status")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    @GetMapping("/getproductbystatus/{productstatus}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProductsByStatus(@PathVariable ProductStatus productstatus,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size){
        log.info("C: Get product based on the product status is called");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Getting all the product based on the status", productService.getAllProductsByStatus(page, size, productstatus)));
    }

    @Operation(summary = "Get all product based on category - PUBLIC", description = "Returns a list of product based on the category")
    @GetMapping("/getproductsbycategory/{productcategory}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProductsByCategory(@PathVariable String productcategory,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size){
        log.info("C: Get product based on the product categories");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Getting all products based on the Category" ,productService.getAllProductsByCategory(productcategory, page, size)));
    }

    @Operation(summary = "Update the product by vendor - ADMIN, VENDOR", description = "Returns a message updated")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    @PutMapping("/updateproduct/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@Valid @RequestBody ProductRequest productRequest, @PathVariable Long id){
        log.info("C: Update the product detail by the vendor");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Product Updated Successfully",productService.updateProduct(id,productRequest)));

    }

    @Operation(summary = "Delete the product based on the ProductId - ADMIN, VENDOR", description = "Returns a message deleted")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    @DeleteMapping("/deleteproduct/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id){
        log.warn("C: Delete product by product id is called");
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Product Deleted", null));
    }

    @Operation(summary = "Update the status of the product with it productId - VENDOR", description = "Returns a Updated message")
    @PreAuthorize("hasRole('VENDOR')")
    @PutMapping("/statusofproduct/{productid}")
    public ResponseEntity<ApiResponse<?>> updateStatusOfTheProduct(@PathVariable String productid){
        log.info("C: update the product Activeness for the product");
        productService.updateStatusOfTheProduct(productid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Status updated",null ));
    }
}
