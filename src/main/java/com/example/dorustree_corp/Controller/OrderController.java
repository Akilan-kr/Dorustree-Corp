package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Enums.OrderStatus;
import com.example.dorustree_corp.Model.MongoDb.OrderData;
import com.example.dorustree_corp.Service.Interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderServiceImplementation;

    @Autowired
    public OrderController(OrderService orderServiceImplementation) {
        this.orderServiceImplementation = orderServiceImplementation;
    }

    @Operation(summary = "Create a order by the user - USER", description = "Returns a message order created")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/createorder") //for user
    public String createOrder(@RequestBody OrderData orderData){
        log.info("C: Creating order end was called by user");
        orderServiceImplementation.createOrder(orderData);
        return "order created";
    }

    @Operation(summary = "Get the order of the user - USER", description = "Returns a ordered details")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getorder") // for user
    public OrderData getOrderOfLoginUser(){
        log.info("C: Get Order is called by user");
        return orderServiceImplementation.getOrderOfLoginUser();
    }

    @Operation(summary = "Update the Order status - VENDOR, USER", description = "Returns a message order Update")
    @PreAuthorize("hasAnyRole('VENDOR', 'USER')")
    @PutMapping("/orderstatus/{orderstatus}") //for user, vendor if vendor means he can confirm the order, user can initiate, cancel and even make the status received
    public String UpdateOrder(@RequestBody OrderData orderData, @PathVariable OrderStatus orderstatus){
        log.info("C: Update Order status is called by either Vendor or User");
        return orderServiceImplementation.updateOrderStatus(orderData, orderstatus);
    }

    @Operation(summary = "Get all order of the user - ADMIN", description = "Returns a list of Ordered Data")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getallorders")//for admin
    public List<OrderData> getAllOrders(){
        log.info("C: Get all Order details called by Admin");
        return orderServiceImplementation.getAllOrders();
    }

    @Operation(summary = "Create a order of the user based on the vendor - ADMIN, VENDOR", description = "Returns a List of order based on the VendorId")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")// got error for both admin and vendor
    @GetMapping("/getallordersbyvendor/{vendorid}")//for vendor, admin
    public List<OrderData> getAllOrdersByVendorId(@PathVariable String vendorid){
        log.info("C: Get all Order based on the Vendor Id: {}",vendorid );
        return orderServiceImplementation.getAllOrdersByVendorId(vendorid);
    }

    @Operation(summary = "Get all order by order status - ADMIN, VENDOR", description = "Returns a list of Ordered Data Based on the Order Status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    @GetMapping("/getallorderbyorderstatus/{orderstatus}")// for admin to view the orderstatus and with the sales related status
    public List<OrderData> getAllOrderByOrderStatus(@PathVariable OrderStatus orderstatus){
        log.info("C: Get all order details by order status is called by Admin");
        return orderServiceImplementation.getAllOrderByOrderStatus(orderstatus);
    }
}
