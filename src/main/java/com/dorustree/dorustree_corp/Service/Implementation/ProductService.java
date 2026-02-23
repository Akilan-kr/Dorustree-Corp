package com.dorustree.dorustree_corp.Service.Implementation;

import com.dorustree.dorustree_corp.Dto.ProductRequest;
import com.dorustree.dorustree_corp.Enums.ProductDeleteStatus;
import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.dorustree.dorustree_corp.Mappers.ProductMapper;
import com.dorustree.dorustree_corp.Model.MySql.Product;
import com.dorustree.dorustree_corp.Repository.MySql.ProductRepository;
import com.dorustree.dorustree_corp.Service.Interfaces.IProductService;
import com.dorustree.dorustree_corp.Service.Interfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    private final IUserService userServiceImplementation;

    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, IUserService userServiceImplementation, ProductMapper productMapper){
        this.productRepository = productRepository;
        this.userServiceImplementation = userServiceImplementation;
        this.productMapper = productMapper;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "activeProducts", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsByStatus", allEntries = true),
            @CacheEvict(value = "productsByVendor", allEntries = true)
    })
    public void addProduct(ProductRequest productRequest) {
        String loggingUserId = userServiceImplementation.findByUserId();
        Product product = productMapper.toEntity(productRequest);
        product.setProductVendorId(loggingUserId);
        log.info("S: Product is add by the Vendor({})", loggingUserId);
        productRepository.save(product);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        log.info("S: Get the product by its Id({})", id);
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElseThrow(() ->
                new RuntimeException("Product not found"));

    }

    @Override
    @Cacheable(value = "activeProducts", key = "#page + '-' + #size + '-' + #search")
    public List<Product> getAllProducts(int page, int size, String search) {

        Page<Product> productPage;

        if (search == null || search.isEmpty()) {
            // Database handles pagination directly
            return productRepository.findAllByProductStatus(
                    PageRequest.of(page, size),
                    ProductStatus.ACTIVE
            );
        } else {
            // Database handles pagination and filtering
            productPage = productRepository.findByProductStatusAndProductNameContainingIgnoreCaseOrProductStatusAndProductCategoryContainingIgnoreCase(
                    ProductStatus.ACTIVE, search,
                    ProductStatus.ACTIVE, search,
                    PageRequest.of(page, size)
            );
        }

        // Convert to list for caching/serialization
        return productPage.getContent();
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
    @Caching(evict = {
            @CacheEvict(value = "activeProducts", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsByStatus", allEntries = true),
            @CacheEvict(value = "productsByVendor", allEntries = true)
    })
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
        String loggingUser = userServiceImplementation.findByUserId();
        log.info("S: Updating the Product for the productId: {}", product.getProductId());
        product.setProductVendorId(loggingUser);
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
