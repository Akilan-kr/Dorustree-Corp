package com.example.dorustree_corp.Service.Implementation;

import com.example.dorustree_corp.Model.MySql.Product;
import com.example.dorustree_corp.Repository.MySql.ProductRepository;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

        private final ProductRepository productRepository;
        private final UserService userServiceImplementation;

        public ExcelService(ProductRepository productRepository, UserService userServiceImplementation) {
            this.productRepository = productRepository;
            this.userServiceImplementation = userServiceImplementation;
        }

        public void importProducts(MultipartFile file) throws IOException {
            String loggingUserId = userServiceImplementation.findByUserId();
            List<Product> batch = new ArrayList<>();
            int batchSize = 500;

            try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

                Sheet sheet = workbook.getSheetAt(0);
                DataFormatter formatter = new DataFormatter();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header

                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    try {
                        Product product = new Product();

                        product.setProductName(formatter.formatCellValue(row.getCell(0)));
                        product.setProductCategory(formatter.formatCellValue(row.getCell(1)));
                        product.setProductPrice(
                                Integer.parseInt(formatter.formatCellValue(row.getCell(2)))
                        );
                        product.setProductQuantity(
                                Integer.parseInt(formatter.formatCellValue(row.getCell(3)))
                        );
                        product.setProductStatus(Boolean.valueOf(formatter.formatCellValue(row.getCell(4))));
                        product.setProductVendorId(loggingUserId);
                        batch.add(product);

                        // Save in batches
                        if (batch.size() >= batchSize) {
                            productRepository.saveAll(batch);
                            batch.clear();
                        }

                    } catch (Exception e) {
                        // Skip bad row instead of crashing
                        System.out.println("Skipping row: " + i);
                    }
                }

                // Save remaining
                if (!batch.isEmpty()) {
                    productRepository.saveAll(batch);
                }
            }
        }
}



