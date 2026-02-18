package com.example.dorustree_corp.Service.Implementation;

import com.example.dorustree_corp.Enums.ProductDeleteStatus;
import com.example.dorustree_corp.Enums.ProductStatus;
import com.example.dorustree_corp.Model.MySql.Product;
import com.example.dorustree_corp.Repository.MySql.ProductRepository;
import com.example.dorustree_corp.Service.Interfaces.ProductService;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;

    private final UserService userServiceImplementation;



    @Autowired
    public ProductServiceImplementation(ProductRepository productRepository, UserService userServiceImplementation){
        this.productRepository = productRepository;
        this.userServiceImplementation = userServiceImplementation;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "activeProducts", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsByStatus", allEntries = true),
            @CacheEvict(value = "productsByVendor", allEntries = true)
    })
    public Product addProduct(Product product) {
        String loggingUserId = userServiceImplementation.findByUserId();
        product.setProductVendorId(loggingUserId);
        log.info("S: Product is add by the Vendor({})", loggingUserId);
        return productRepository.save(product);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        log.info("S: Get the product by its Id({})", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    @Cacheable(value = "activeProducts")
    public List<Product> getAllProducts(int page, int size) {
        log.info("S: Get all Products");
        return productRepository.findAllByProductStatus(PageRequest.of(page, size),ProductStatus.ACTIVE);
    }

    @Override
    @Cacheable(value = "productsByCategory", key = "#productCategory")
    public List<Product> getAllProductsByCategory(String productCategory) {
        log.info("S: Get all Product Based on the Category({})", productCategory);
        return productRepository.findAllByProductCategory(productCategory);
    }

    @Override
    @Cacheable(value = "productsByStatus", key = "#productstatus")
    public List<Product> getAllProductsByStatus(int page, int size, ProductStatus productstatus) {
        log.info("S: Get All Product by the Status({})", productstatus);
        return productRepository.findAllByProductStatus(PageRequest.of(page, size), productstatus);
    }

    @Override
    @Cacheable(value = "productsByVendor", key = "#productvendorid")
    public List<Product> getAllProductsUsingVendorId(int page, int size, String productvendorid) {
        log.info("S: Get All the Product based on the vendorId({})", productvendorid);
        return productRepository.getAllByProductVendorId(PageRequest.of(page, size), productvendorid);
    }

    @Override
    public List<Product> getAllProductForLoginVendor(int page, int size) {
        log.info("S: Get All products of the login vendor({})", userServiceImplementation.findByUserId());
        return getAllProductsUsingVendorId(page, size, userServiceImplementation.findByUserId());
    }

    @Override
    public Integer getProductPrice(String id) {
        log.info("S: Get Price of the Product({})", id);
        return getProductById(Long.valueOf(id)).getProductPrice();
    }

    @Override
    public void updateStatusOfTheProduct(String productid) {
        log.info("S: Update the Product Status");
        Product product = getProductById(Long.valueOf(productid));
        if(product.getProductStatus() == ProductStatus.ACTIVE){
            product.setProductStatus(ProductStatus.INACTIVE);
        } else if (product.getProductStatus() == ProductStatus.INACTIVE) {
            product.setProductStatus(ProductStatus.ACTIVE);
        }
        updateProduct(product);
    }


    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "product", key = "#product.productId"),
                    @CacheEvict(value = "activeProducts", allEntries = true),
                    @CacheEvict(value = "productsByCategory", allEntries = true),
                    @CacheEvict(value = "productsByStatus", allEntries = true),
                    @CacheEvict(value = "productsByVendor", allEntries = true)
            }
    )
    public void updateProduct(Product product) {
        log.info("S: Updating the Product for the productId: {}", product.getProductId());
        productRepository.save(product);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "product", key = "#id"),
            @CacheEvict(value = "activeProducts", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsByStatus", allEntries = true),
            @CacheEvict(value = "productsByVendor", allEntries = true)
    })
    public void deleteProductById(Long id) {
        Product product = getProductById(id);
        product.setProductDeleteStatus(ProductDeleteStatus.DELETED);
        log.warn("S: Deleting the product for a Id: {}", id);
        productRepository.save(product);
    }




}
