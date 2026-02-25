package com.dorustree.dorustree_corp.Controller;

import com.dorustree.dorustree_corp.Dto.ApiResponse;
import com.dorustree.dorustree_corp.Dto.OrderRequest;
import com.dorustree.dorustree_corp.Dto.OrderResponse;
import com.dorustree.dorustree_corp.Enums.OrderStatus;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderData;
import com.dorustree.dorustree_corp.Service.Interfaces.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final IOrderService OrderService;

    @Autowired
    public OrderController(IOrderService OrderService) {
        this.OrderService = OrderService;
    }

    @Operation(summary = "Create a order by the user - USER", description = "Returns a message order created")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/placeorder") //for user
    public ResponseEntity<ApiResponse<?>> placeOrder(@RequestBody OrderRequest orderRequest){
        log.info("C: Creating order end was called by user");
        OrderService.placeOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Order Placed", null));
    }

    @Operation(summary = "Get the order of the user - USER", description = "Returns a ordered details")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getorder") // for user
    public ResponseEntity<List<OrderData>> getOrderOfLoginUser(){
        log.info("C: Get Order is called by user");
        return ResponseEntity.ok(OrderService.getOrderOfLoginUser());
    }

    @Operation(summary = "Update the Order status - VENDOR, USER", description = "Returns a message order Update")
    @PreAuthorize("hasAnyRole('VENDOR', 'USER')")
    @PutMapping("/orderstatus/{orderstatus}") //for user, vendor if vendor means he can confirm the order, user can initiate, cancel and even make the status received
    public ResponseEntity<?> UpdateOrder(@RequestBody OrderRequest orderRequest, @PathVariable OrderStatus orderstatus){
        log.info("C: Update Order status is called by either Vendor or User");
        boolean status = OrderService.updateOrderStatus(orderRequest, orderstatus);
        if(status)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }
    @Operation(summary = "Update the Order status using orderId - VENDOR, USER", description = "Returns a message order Update")
    @PreAuthorize("hasAnyRole('VENDOR', 'USER')")
    @PutMapping("/updateorderstatus/{orderId}/{orderstatus}")
    public ResponseEntity<ApiResponse<?>> updateOrderstatus(@PathVariable String orderId,@PathVariable OrderStatus orderstatus){
        log.info("C: update order status is called by either Vendor or User");
        OrderService.updateOrderStatusById(orderId, orderstatus);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Status Updated", null));
    }

    @Operation(summary = "Get all order of the user - ADMIN", description = "Returns a list of Ordered Data")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getallorders")//for admin
    public ResponseEntity<List<OrderData>> getAllOrders(){
        log.info("C: Get all Order details called by Admin");
        return ResponseEntity.ok(OrderService.getAllOrders());
    }

    @Operation(summary = "Create a order of the user based on the vendor - ADMIN, VENDOR", description = "Returns a List of order based on the VendorId")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")// got error for both admin and vendor
    @GetMapping("/getallordersbyvendor")//for vendor, admin
    public ResponseEntity<List<OrderResponse>> getAllOrdersByVendorId(){
        log.info("C: Get all Order based on the login Vendor " );
        return ResponseEntity.ok(OrderService.getAllOrdersByVendorId());
    }

    @Operation(summary = "Get all order by order status - ADMIN, VENDOR", description = "Returns a list of Ordered Data Based on the Order Status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    @GetMapping("/getallorderbyorderstatus/{orderstatus}")// for admin to view the orderstatus and with the sales related status
    public ResponseEntity<List<OrderResponse>> getAllOrderByOrderStatus(@PathVariable OrderStatus orderstatus){
        log.info("C: Get all order details by order status is called by Admin");
        return ResponseEntity.ok(OrderService.getAllOrderByOrderStatus(orderstatus));
    }
}
