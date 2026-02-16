package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Enums.OrderStatus;
import com.example.dorustree_corp.Model.MongoDb.OrderData;
import com.example.dorustree_corp.Service.Interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderServiceImplementation;

    @Autowired
    public OrderController(OrderService orderServiceImplementation) {
        this.orderServiceImplementation = orderServiceImplementation;
    }

    @GetMapping("/createorder") //for user
    public String createOrder(@RequestBody OrderData orderData){
        orderServiceImplementation.createOrder(orderData);
        return "order created";
    }

    @GetMapping("/getorder") // for user
    public OrderData getOrderOfLoginUser(){
        return orderServiceImplementation.getOrderOfLoginUser();
    }

    @PutMapping("/orderstatus/{orderstatus}") //for user, vendor if vendor means he can confirm the order, user can initiate, cancel and even make the status received
    public String cancelOrder(@RequestBody OrderData orderData, @PathVariable OrderStatus orderstatus){
        return orderServiceImplementation.updateOrderStatus(orderData, orderstatus);
    }

    @GetMapping("/getallorders")//for admin
    public List<OrderData> getAllOrders(){
        return orderServiceImplementation.getAllOrders();
    }

    @GetMapping("/getallordersbyvendor/{vendorid}")//for vendor, admin
    public List<OrderData> getAllOrdersByVendorId(@PathVariable String vendorid){
        return orderServiceImplementation.getAllOrdersByVendorId(vendorid);
    }

    @GetMapping("/getallorderbyorderstatus/{orderstatus}")// for admin to view the orderstatus and with the sales related status
    public List<OrderData> getAllOrderByOrderStatus(@PathVariable OrderStatus orderstatus){
        return orderServiceImplementation.getAllOrderByOrderStatus(orderstatus);
    }
}
