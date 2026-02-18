package com.example.dorustree_corp.Service.Implementation;

import com.example.dorustree_corp.Enums.OrderStatus;
import com.example.dorustree_corp.Model.MongoDb.OrderData;
import com.example.dorustree_corp.Model.MongoDb.UserData;
import com.example.dorustree_corp.Model.MySql.Product;
import com.example.dorustree_corp.Dto.OrderItems;
import com.example.dorustree_corp.Repository.MongoDb.OrderRepository;
import com.example.dorustree_corp.Service.EmailService;
import com.example.dorustree_corp.Service.Interfaces.OrderService;
import com.example.dorustree_corp.Service.Interfaces.ProductService;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImplementation implements OrderService {

    private final UserService userServiceImplementation;
    private final OrderRepository orderRepository;
    private final ProductService productServiceImplementation;
    private final EmailService emailService;

    @Autowired
    public OrderServiceImplementation(UserService userServiceImplementation, OrderRepository orderRepository, ProductService productServiceImplementation, EmailService emailService) {
        this.userServiceImplementation = userServiceImplementation;
        this.orderRepository = orderRepository;
        this.productServiceImplementation = productServiceImplementation;
        this.emailService = emailService;
    }


    @Override
    public void createOrder(OrderData orderData) {
//        System.out.println("Main Thread: " + Thread.currentThread().getName());

        String loggingUserId = userServiceImplementation.findByUserId();
//        System.out.println(loggingUserId);
        UserData user = userServiceImplementation.getUserById(loggingUserId);
//        System.out.println(user.getUserEmail());
        int totalPrice = 0;

        orderData.setOrderedUserId(loggingUserId);
        orderData.setOrderStatus(OrderStatus.Order_Initiated);

        for (OrderItems item : orderData.getOrderedItems()) {

            Long productId = Long.valueOf(item.getProductId());
            Integer quantity = item.getProductQuantity();

            Product product = productServiceImplementation.getProductById(productId);

            // ✅ Validate vendor
            if (!product.getProductVendorId().equals(item.getProductVendorId())) {
                throw new RuntimeException("Vendor mismatch for product: " + productId);
            }

            // ✅ Validate stock
            if (product.getProductQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + productId);
            }

            // ✅ Always use DB price
            int productPrice = product.getProductPrice();

            totalPrice += productPrice * quantity;

            // Reduce stock
            product.setProductQuantity(product.getProductQuantity() - quantity);
            productServiceImplementation.updateProduct(product);

            // Optional: store actual price into order item
            item.setProductPrice(productPrice);
        }

        orderData.setTotalPrice(totalPrice);
        log.info("S: Order is Created by the user({}) with a orderId:{}",loggingUserId,orderData.getId());
        orderRepository.save(orderData);
        emailService.sendOrderConfirmation(user.getUserEmail(), orderData.getId());
    }



    @Override
    public OrderData getOrderOfLoginUser() {
        log.info("S: Getting Order Detail for the user({})", userServiceImplementation.findByUserId());
        return orderRepository.findByOrderedUserId(userServiceImplementation.findByUserId());
    }

    @Override
    public String updateOrderStatus(OrderData orderData, OrderStatus orderstatus) {
        String loginUser = userServiceImplementation.findByUserId();
        UserData user = userServiceImplementation.getUserById(loginUser);
        if(OrderStatus.Order_Cancel == orderstatus || OrderStatus.Order_Received == orderstatus) {
            orderData.setOrderStatus(orderstatus);
            log.info("S: Updating Order Status of the User({}) with a OrderId({}) as {}", loginUser, orderData.getId(), orderstatus);
            orderRepository.save(orderData);
            if(OrderStatus.Order_Cancel == orderstatus){
                emailService.sendOrderCancellation(user.getUserEmail(), orderData.getId());
            }
            return "status updated";
        } else
            return "status not updated";
    }

    @Override
    public List<OrderData> getAllOrders() {
        log.info("S: Get list of Orders");
        return orderRepository.findAll();
    }

    @Override
    public List<OrderData> getAllOrdersByVendorId(String vendorid) {
        log.info("S: Get all the Orders Based on the VendorId({})", vendorid);
        return orderRepository.findByOrderedItemsProductVendorId(vendorid);
    }

    @Override
    public List<OrderData> getAllOrderByOrderStatus(OrderStatus orderstatus) {
        log.info("S: Get all Orders By OrderStatus with a status of {}", orderstatus);
        return orderRepository.findAllByOrderStatus(orderstatus);
    }

}
