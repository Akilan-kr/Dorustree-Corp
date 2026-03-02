package com.dorustree.dorustree_corp.Service;

import com.dorustree.dorustree_corp.Dto.DashboardDTO;

import com.dorustree.dorustree_corp.Enums.*;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderData;
import com.dorustree.dorustree_corp.Repository.MongoDb.OrderRepository;
import com.dorustree.dorustree_corp.Repository.MongoDb.UserRepository;
import com.dorustree.dorustree_corp.Repository.MySql.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardDTO getDashboardStats() {
        DashboardDTO dto = new DashboardDTO();

        log.info("S: Service for admin Dashboard is called by admin");
        LocalDate today = LocalDate.now();
        Instant startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().minusMillis(1);

        // Users
        dto.setTotalUsers(userRepository.count());
        dto.setTotalVendors(userRepository.countByUserRole(UserRoles.VENDOR));
        dto.setPendingVendorRequests(userRepository.countByUserStatusForVendor(UserStatusForVendor.Status_Pending));

        // Total products (excluding deleted)
        dto.setTotalProducts(
                productRepository.countByProductDeleteStatus(ProductDeleteStatus.NOT_DELETED)
        );

        // Active products
        dto.setActiveProducts(
                productRepository.countByProductStatusAndProductDeleteStatus(
                        ProductStatus.ACTIVE,
                        ProductDeleteStatus.NOT_DELETED
                )
        );

        // Inactive products
        dto.setInactiveProducts(
                productRepository.countByProductStatusAndProductDeleteStatus(
                        ProductStatus.INACTIVE,
                        ProductDeleteStatus.NOT_DELETED
                )
        );

        // Orders
        dto.setTotalOrders(orderRepository.count());
        dto.setCompletedOrders(orderRepository.countByOrderStatus(OrderStatus.Order_Received));
        dto.setPendingOrders(orderRepository.countByOrderStatus(OrderStatus.Order_Initiated));

        // Revenue
        double revenueToday = orderRepository
                .findByOrderStatusAndOrderDateBetween(OrderStatus.Order_Received, startOfDay, endOfDay)
                .stream().mapToDouble(OrderData::getTotalPrice).sum();

        double revenueTotal = orderRepository
                .findByOrderStatusAndOrderDateBetween(OrderStatus.Order_Received,
                        LocalDate.of(2026, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                        Instant.now())
                .stream().mapToDouble(OrderData::getTotalPrice).sum();

        dto.setRevenueTotal(revenueTotal);
        dto.setRevenueToday(revenueToday);

        return dto;
    }
}
