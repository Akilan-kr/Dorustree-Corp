package com.dorustree.dorustree_corp.Controller;

import com.dorustree.dorustree_corp.Model.MongoDb.CartData;
import com.dorustree.dorustree_corp.Service.Interfaces.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ICartService CartService;

    @Autowired
    public CartController(ICartService CartServiceImplementation) {
        this.CartService = CartServiceImplementation;
    }

    @Operation(summary = "Add to Cart by the User - USER", description = "Returns a message cart Added")
    @PostMapping("/addtocart")
    @PreAuthorize("hasAnyRole('USER', 'VENDOR')")
    public ResponseEntity<?> addToCart(@RequestBody CartData cartData){
        log.info("C: Add to Cart is called by a user");
//        System.out.println(cartData.getItems().toString());
//        if (cartData.getItems() == null) {
//            cartData.setItems(new HashMap<>());
//        }
        CartService.addToCart(cartData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Remove from Cart by the User - USER", description = "Returns a message cart Removed")
    @PostMapping("/removefromcart")
    @PreAuthorize("hasAnyRole('USER', 'VENDOR')")
    public ResponseEntity<?> removeFromCart(@RequestBody CartData cartData){
        log.info("C: Remove from Cart is called by a user");
        CartService.removeFromCart(cartData);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Get user their cart - USER", description = "Returns a cart Data")
    @GetMapping("/getcart")
    @PreAuthorize("hasAnyRole('USER', 'VENDOR')")
    public ResponseEntity<CartData> getCart(){
        log.info("C: Get Cart is called by user");
        return ResponseEntity.ok(CartService.getCart());
    }

    @Operation(summary = "deleted the cart - USER", description = "Returns a message cart Deleted")
    @DeleteMapping("/deletecart")
    @PreAuthorize("hasAnyRole('USER', 'VENDOR')")
    public ResponseEntity<?> deleteCart(){
        log.info("C: Cart is deleted by the user");
        CartService.deleteCart();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
