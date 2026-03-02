package com.dorustree.dorustree_corp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    // Users
    private long totalUsers;
    private long totalVendors;
    private long pendingVendorRequests;

    // Products
    private long totalProducts;
    private long activeProducts;
    private long inactiveProducts;

    // Orders
    private long totalOrders;
    private long completedOrders;
    private long pendingOrders;

    // Revenue
    private double revenueToday;
    private double revenueTotal;
}
