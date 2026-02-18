package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Model.MongoDb.CartData;
import com.example.dorustree_corp.Service.Interfaces.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartServiceImplementation;

    @Autowired
    public CartController(CartService cartServiceImplementation) {
        this.cartServiceImplementation = cartServiceImplementation;
    }

    @Operation(summary = "Add to Cart by the User - USER", description = "Returns a message cart Added")
    @PostMapping("/addtocart")
    public String addToCart(@RequestBody CartData cartData){
        log.info("C: Add to Cart is called by a user");
        cartServiceImplementation.addToCart(cartData);
        return "cart Added";
    }

    @Operation(summary = "Remove from Cart by the User - USER", description = "Returns a message cart Removed")
    @PostMapping("/removefromcart")
    public String removeFromCart(@RequestBody CartData cartData){
        log.info("C: Remove from Cart is called by a user");
        cartServiceImplementation.removeFromCart(cartData);
        return "cart Remove";
    }

    @Operation(summary = "Get user their cart - USER", description = "Returns a cart Data")
    @GetMapping("/getcart")
    public CartData getCart(){
        log.info("C: Get Cart is called by user");
        return cartServiceImplementation.getCart();
    }

    @Operation(summary = "deleted the cart - USER", description = "Returns a message cart Deleted")
    @DeleteMapping("/deletecart")
    public String deleteCart(){
        log.info("C: Cart is deleted by the user");
        cartServiceImplementation.deleteCart();
        return "cart deleted";
    }
}
