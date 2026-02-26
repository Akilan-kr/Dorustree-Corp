package com.dorustree.dorustree_corp.Service.Implementation;

import com.dorustree.dorustree_corp.Dto.ProductRequest;
import com.dorustree.dorustree_corp.Dto.ProductResponse;
import com.dorustree.dorustree_corp.Enums.ProductDeleteStatus;
import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.dorustree.dorustree_corp.Mappers.ProductMapper;
import com.dorustree.dorustree_corp.Model.MySql.Product;
import com.dorustree.dorustree_corp.Repository.MySql.ProductRepository;
import com.dorustree.dorustree_corp.Service.Interfaces.IProductService;
import com.dorustree.dorustree_corp.Service.Interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final IUserService userServiceImplementation;
    private final ProductMapper productMapper;

    // ================================
    // ADD PRODUCT
    // ================================
    @Override
    @Caching(evict = {
            @CacheEvict(value = "activeProducts", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsByStatus", allEntries = true),
            @CacheEvict(value = "productsByVendor", allEntries = true)
    })
    public ProductResponse addProduct(ProductRequest request) {

        String loggingUserId = userServiceImplementation.findByUserId();

        Product product = productMapper.toEntity(request);
        product.setProductVendorId(loggingUserId);
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setProductDeleteStatus(ProductDeleteStatus.NOT_DELETED);

        Product savedProduct = productRepository.save(product);

        log.info("S: Product added by Vendor({})", loggingUserId);

        return productMapper.toResponse(savedProduct);
    }

    // ================================
    // GET PRODUCT BY ID
    // ================================
    @Override
//    @Cacheable(value = "product", key = "#productId")
    public ProductResponse getProductById(Long productId) {
        log.info("S: Get product by Id({})", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toResponse(product);
    }

    // ================================
    // GET ALL ACTIVE PRODUCTS
    // ================================
    @Override
    @Cacheable(value = "activeProducts", key = "#page + '_' + #size + '_' + #search")
    public List<ProductResponse> getAllProducts(int page, int size, String search) {
        var productPage = (search == null || search.isEmpty())
                ? productRepository.findAllByProductStatus(ProductStatus.ACTIVE, PageRequest.of(page, size))
                : productRepository.findByProductStatusAndProductNameContainingIgnoreCaseOrProductStatusAndProductCategoryContainingIgnoreCase(
                ProductStatus.ACTIVE, search,
                ProductStatus.ACTIVE, search,
                PageRequest.of(page, size)
        );

        return productPage.getContent().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    // ================================
    // GET BY CATEGORY
    // ================================
    @Override
    @Cacheable(value = "productsByCategory", key = "#productCategory + '_' + #page + '_' + #size")
    public List<ProductResponse> getAllProductsByCategory(String productCategory, int page, int size) {
        log.info("S: Get products by Category({})", productCategory);
        return productRepository.findAllByProductCategory(productCategory, PageRequest.of(page, size))
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    // ================================
    // GET BY STATUS
    // ================================
    @Override
    @Cacheable(value = "productsByStatus", key = "#productStatus + '_' + #page + '_' + #size")
    public List<ProductResponse> getAllProductsByStatus(int page, int size, ProductStatus productStatus) {
        log.info("S: Get products by Status({})", productStatus);
        return productRepository.findAllByProductStatus(productStatus, PageRequest.of(page, size))
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    // ================================
    // GET BY VENDOR
    // ================================
    @Override
//    @Cacheable(value = "productsByVendor", key = "#vendorId + '_' + #page + '_' + #size")
    public List<ProductResponse> getAllProductsUsingVendorId(int page, int size, String vendorId) {
        log.info("S: Get products by Vendor({})", vendorId);
        return productRepository.getAllByProductVendorId(PageRequest.of(page, size), vendorId)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getAllProductForLoginVendor(int page, int size) {

        String loggedUser = userServiceImplementation.findByUserId();
        Pageable pageable = PageRequest.of(page, size);

        return productRepository
                .findAllByProductVendorIdAndProductDeleteStatus(
                        loggedUser,
                        ProductDeleteStatus.NOT_DELETED,
                        pageable
                )
                .map(productMapper::toResponse).toList();
    }

    // ================================
    // UPDATE PRODUCT
    // ================================
    @Override
    @Caching(evict = {
            @CacheEvict(value = "product", key = "#productId"),
            @CacheEvict(value = "activeProducts", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsByStatus", allEntries = true),
            @CacheEvict(value = "productsByVendor", allEntries = true)
    })
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        log.info("S: Updating product with Id({})", productId);

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setProductName(request.productName());
        existingProduct.setProductCategory(request.productCategory());
        existingProduct.setProductPrice(request.productPrice());
        existingProduct.setProductQuantity(request.productQuantity());

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponse(updatedProduct);
    }

    // ================================
    // TOGGLE PRODUCT STATUS
    // ================================
    @Override
    @Caching(evict = {
            @CacheEvict(value = "product", key = "#productId"),
            @CacheEvict(value = "activeProducts", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsByStatus", allEntries = true),
            @CacheEvict(value = "productsByVendor", allEntries = true)
    })
    public void updateStatusOfTheProduct(String productId) {
        Product product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductStatus(
                product.getProductStatus() == ProductStatus.ACTIVE
                        ? ProductStatus.INACTIVE
                        : ProductStatus.ACTIVE
        );

        productRepository.save(product);
        log.info("S: Product status toggled for Id({})", productId);
    }

    // ================================
    // SOFT DELETE PRODUCT
    // ================================
    @Override
    @Caching(evict = {
            @CacheEvict(value = "product", key = "#productId"),
            @CacheEvict(value = "activeProducts", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsByStatus", allEntries = true),
            @CacheEvict(value = "productsByVendor", allEntries = true)
    })
    public void deleteProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductDeleteStatus(ProductDeleteStatus.DELETED);
        product.setProductStatus(ProductStatus.INACTIVE);
        productRepository.save(product);

        log.warn("S: Product soft deleted with Id({})", productId);
    }
}
