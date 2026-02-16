package com.example.dorustree_corp.Service.Implementation;

import com.example.dorustree_corp.Enums.OrderStatus;
import com.example.dorustree_corp.Model.MongoDb.OrderData;
import com.example.dorustree_corp.Model.MySql.Product;
import com.example.dorustree_corp.Model.OrderItems;
import com.example.dorustree_corp.Repository.MongoDb.OrderRepository;
import com.example.dorustree_corp.Service.Interfaces.OrderService;
import com.example.dorustree_corp.Service.Interfaces.ProductService;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class OrderServiceImplementation implements OrderService {

    private final UserService userServiceImplementation;
    private final OrderRepository orderRepository;
    private final ProductService productServiceImplementation;

    @Autowired
    public OrderServiceImplementation(UserService userServiceImplementation, OrderRepository orderRepository, ProductService productServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
        this.orderRepository = orderRepository;
        this.productServiceImplementation = productServiceImplementation;
    }


    @Override
    public void createOrder(OrderData orderData) {

        String loggingUserId = userServiceImplementation.findByUserId();
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

        orderRepository.save(orderData);
    }



    @Override
    public OrderData getOrderOfLoginUser() {
        return orderRepository.findByOrderedUserId(userServiceImplementation.findByUserId());
    }

    @Override
    public String updateOrderStatus(OrderData orderData, OrderStatus orderstatus) {
        if(OrderStatus.Order_Cancel == orderstatus || OrderStatus.Order_Received == orderstatus) {
            orderData.setOrderStatus(orderstatus);
            orderRepository.save(orderData);
            return "status updated";
        } else
            return "status not updated";
    }

    @Override
    public List<OrderData> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderData> getAllOrdersByVendorId(String vendorid) {
        return orderRepository.findByOrderedItemsProductVendorId(vendorid);
    }

    @Override
    public List<OrderData> getAllOrderByOrderStatus(OrderStatus orderstatus) {
        return orderRepository.findAllByOrderStatus(orderstatus);
    }

}
