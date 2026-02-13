package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Model.MongoDb.CartData;
import com.example.dorustree_corp.Service.Interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartServiceImplementation;

    @Autowired
    public CartController(CartService cartServiceImplementation) {
        this.cartServiceImplementation = cartServiceImplementation;
    }


    @PostMapping("/addtocart")
    public String addToCart(@RequestBody CartData cartData){
        cartServiceImplementation.addToCart(cartData);
        return "cart Added";
    }

    @GetMapping("/getcart")
    public CartData getCart(){
        return cartServiceImplementation.getCart();
    }

    @DeleteMapping("/deletecart")
    public String deleteCart(){
        cartServiceImplementation.deleteCart();
        return "cart deleted";
    }
}
