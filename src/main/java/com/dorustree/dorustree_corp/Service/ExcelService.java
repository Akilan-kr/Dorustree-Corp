package com.dorustree.dorustree_corp.Service;

import com.dorustree.dorustree_corp.Enums.ProductStatus;
import com.dorustree.dorustree_corp.Model.MySql.Product;
import com.dorustree.dorustree_corp.Repository.MySql.ProductRepository;
import com.dorustree.dorustree_corp.Service.Interfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExcelService {

    private final ProductRepository productRepository;
    private final IUserService userServiceImplementation;

    public ExcelService(ProductRepository productRepository, IUserService userServiceImplementation) {
        this.productRepository = productRepository;
        this.userServiceImplementation = userServiceImplementation;
    }

    @Caching(evict = {
            @CacheEvict(value = "activeProducts", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsByStatus", allEntries = true),
            @CacheEvict(value = "productsByVendor", allEntries = true)
    })
    @Transactional
    public void importProducts(MultipartFile file) throws Exception {

        String loggingUserId = userServiceImplementation.findByUserId();
        List<Product> batch = new ArrayList<>();
        int batchSize = 500;

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                try {
                    Product product = new Product();
                    product.setProductName(formatter.formatCellValue(row.getCell(0)));
                    product.setProductCategory(formatter.formatCellValue(row.getCell(1)));
                    product.setProductPrice(Integer.parseInt(formatter.formatCellValue(row.getCell(2))));
                    product.setProductQuantity(Integer.parseInt(formatter.formatCellValue(row.getCell(3))));
                    product.setProductStatus(ProductStatus.valueOf(formatter.formatCellValue(row.getCell(4))));
                    product.setProductVendorId(loggingUserId);

                    batch.add(product);

                    // Batch save
                    if (batch.size() >= batchSize) {
                        productRepository.saveAll(batch);
                        batch.clear();
                    }
                } catch (Exception e) {
                    log.warn("Skipping row {} due to error: {}", row.getRowNum(), e.getMessage());
                }
            }

            if (!batch.isEmpty()) {
                productRepository.saveAll(batch);
            }

            log.info("S: Excel upload completed successfully by user {}", loggingUserId);
        }
    }
}
