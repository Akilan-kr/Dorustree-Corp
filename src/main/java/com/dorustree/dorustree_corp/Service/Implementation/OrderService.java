package com.dorustree.dorustree_corp.Service.Implementation;

import com.dorustree.dorustree_corp.Dto.OrderRequest;
import com.dorustree.dorustree_corp.Dto.OrderResponse;
import com.dorustree.dorustree_corp.Enums.OrderStatus;
import com.dorustree.dorustree_corp.Mappers.OrderMapper;
import com.dorustree.dorustree_corp.Mappers.ProductMapper;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderData;
import com.dorustree.dorustree_corp.Model.MongoDb.UserData;
import com.dorustree.dorustree_corp.Model.MySql.Product;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderItems;
import com.dorustree.dorustree_corp.Repository.MongoDb.OrderRepository;
import com.dorustree.dorustree_corp.Repository.MySql.ProductRepository;
import com.dorustree.dorustree_corp.Service.EmailService;
import com.dorustree.dorustree_corp.Service.Interfaces.IOrderService;
import com.dorustree.dorustree_corp.Service.Interfaces.IUserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService implements IOrderService {

    private final IUserService userServiceImplementation;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final OrderMapper orderMapper;
    private final UserService userService;

    @Autowired
    public OrderService(IUserService userServiceImplementation, OrderRepository orderRepository, ProductRepository productRepository, EmailService emailService, ProductMapper productMapper, OrderMapper orderMapper, UserService userService) {
        this.userServiceImplementation = userServiceImplementation;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
        this.orderMapper = orderMapper;
        this.userService = userService;
    }


    @Override
    @Transactional
    public void placeOrder(OrderRequest orderRequest) {

        String loggingUserId = userServiceImplementation.findByUserId();
        UserData user = userServiceImplementation.getUserById(loggingUserId);

        // ✅ Use mapper here
        OrderData orderData = orderMapper.toEntity(orderRequest, loggingUserId);

        int totalPrice = 0;

        for (OrderItems item : orderData.getOrderedItems()) {

            Long productId = Long.valueOf(item.getProductId());
            Integer quantity = item.getProductQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // ✅ Validate stock
            if (product.getProductQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + productId);
            }

            // ✅ Always use DB price
            int productPrice = product.getProductPrice();
            totalPrice += productPrice * quantity;

            // ✅ Set vendor from product (not from user)
            item.setProductVendorId(product.getProductVendorId());

            // ✅ Set actual DB price
            item.setProductPrice(productPrice);

            // ✅ Reduce stock
            product.setProductQuantity(product.getProductQuantity() - quantity);
            productRepository.save(product);
        }

        orderData.setTotalPrice(totalPrice);
        orderData.setOrderDate(Instant.now());

        orderRepository.save(orderData);

        log.info("S: Order Created by user({}) with orderId: {}", loggingUserId, orderData.getId());

        emailService.sendOrderConfirmation(user.getUserEmail(), orderData.getId());
    }





    @Override
    public List<OrderData> getOrderOfLoginUser() {
        log.info("S: Getting Order Detail for the user({})", userServiceImplementation.findByUserId());
        return orderRepository.findByOrderedUserId(userServiceImplementation.findByUserId());
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(OrderRequest orderRequest, OrderStatus orderStatus) {

        String loginUserId = userServiceImplementation.findByUserId();
        UserData user = userServiceImplementation.getUserById(loginUserId);

        OrderData orderData = orderMapper.toEntity(orderRequest, loginUserId);

        if (orderStatus == OrderStatus.Order_Cancel || orderStatus == OrderStatus.Order_Received) {

            orderData.setOrderStatus(orderStatus);

            log.info("S: Updating Order Status for User({}) with OrderId({}) to {}",
                    loginUserId, orderData.getId(), orderStatus);

            orderRepository.save(orderData);

            if (orderStatus == OrderStatus.Order_Cancel) {
                emailService.sendOrderCancellation(user.getUserEmail(), orderData.getId());
            }

            return true;
        }

        return false;
    }


    @Override
    public List<OrderData> getAllOrders() {
        log.info("S: Get list of Orders");
        return orderRepository.findAll();
    }

    @Override
    public List<OrderResponse> getAllOrdersByVendorId() {

        String vendorId = userService.findByUserId();

        log.info("S: Get all Orders for Vendor({})", vendorId);

        List<OrderData> orders = orderRepository
                .findByOrderedItemsProductVendorId(vendorId);

        return orders.stream()
                .map(order -> orderMapper.toResponse(order, vendorId))
                .toList();
    }

    @Override
    public List<OrderResponse> getAllOrderByOrderStatus(OrderStatus orderStatus) {

        String vendorId = userService.findByUserId();

        log.info("S: Get all Orders for Vendor({}) with status {}", vendorId, orderStatus);

        List<OrderData> orders = orderRepository
                .findByVendorAndStatus(vendorId, orderStatus);

        return orders.stream()
                .map(order -> orderMapper.toResponse(order, vendorId))
                .toList();
    }

    @Override
    public void updateOrderStatusById(String orderId, OrderStatus orderStatus) {
        Optional<OrderData> order = orderRepository.findById(orderId); // <-- use String directly
        if (order.isEmpty()) {
            throw new RuntimeException("No Order found with orderId: " + orderId);
        }

        OrderData orderData = order.get();
        orderData.setOrderStatus(orderStatus);
        orderRepository.save(orderData);
    }




}
