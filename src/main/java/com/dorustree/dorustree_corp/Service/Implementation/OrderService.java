package com.dorustree.dorustree_corp.Service.Implementation;

import com.dorustree.dorustree_corp.Enums.OrderStatus;
import com.dorustree.dorustree_corp.Mappers.ProductMapper;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderData;
import com.dorustree.dorustree_corp.Model.MongoDb.UserData;
import com.dorustree.dorustree_corp.Model.MySql.Product;
import com.dorustree.dorustree_corp.Dto.OrderItems;
import com.dorustree.dorustree_corp.Repository.MongoDb.OrderRepository;
import com.dorustree.dorustree_corp.Repository.MySql.ProductRepository;
import com.dorustree.dorustree_corp.Service.EmailService;
import com.dorustree.dorustree_corp.Service.Interfaces.IOrderService;
import com.dorustree.dorustree_corp.Service.Interfaces.IProductService;
import com.dorustree.dorustree_corp.Service.Interfaces.IUserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderService implements IOrderService {

    private final IUserService userServiceImplementation;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    @Autowired
    public OrderService(IUserService userServiceImplementation, OrderRepository orderRepository, ProductRepository productRepository, EmailService emailService, ProductMapper productMapper) {
        this.userServiceImplementation = userServiceImplementation;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
    }


    @Override
    @Transactional
    public void placeOrder(OrderData orderData) {

        String loggingUserId = userServiceImplementation.findByUserId();
        UserData user = userServiceImplementation.getUserById(loggingUserId);

        int totalPrice = 0;

        orderData.setOrderedUserId(loggingUserId);
        orderData.setOrderStatus(OrderStatus.Order_Initiated);

        for (OrderItems item : orderData.getOrderedItems()) {

            Long productId = Long.valueOf(item.getProductId());
            Integer quantity = item.getProductQuantity();

            // ✅ Fetch entity directly from repository
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

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

            // ✅ Reduce stock
            product.setProductQuantity(product.getProductQuantity() - quantity);
            productRepository.save(product);

            // ✅ Store actual price into order item
            item.setProductPrice(productPrice);
        }

        orderData.setTotalPrice(totalPrice);

        orderRepository.save(orderData);

        log.info("S: Order Created by user({}) with orderId: {}", loggingUserId, orderData.getId());

        emailService.sendOrderConfirmation(user.getUserEmail(), orderData.getId());
    }



    @Override
    public OrderData getOrderOfLoginUser() {
        log.info("S: Getting Order Detail for the user({})", userServiceImplementation.findByUserId());
        return orderRepository.findByOrderedUserId(userServiceImplementation.findByUserId());
    }

    @Override
    public boolean updateOrderStatus(OrderData orderData, OrderStatus orderstatus) {
        String loginUser = userServiceImplementation.findByUserId();
        UserData user = userServiceImplementation.getUserById(loginUser);
        if(OrderStatus.Order_Cancel == orderstatus || OrderStatus.Order_Received == orderstatus) {
            orderData.setOrderStatus(orderstatus);
            log.info("S: Updating Order Status of the User({}) with a OrderId({}) as {}", loginUser, orderData.getId(), orderstatus);
            orderRepository.save(orderData);
            if(OrderStatus.Order_Cancel == orderstatus){
                emailService.sendOrderCancellation(user.getUserEmail(), orderData.getId());
            }
            return true;
        } else
            return false;
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
