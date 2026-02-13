package com.example.dorustree_corp.Service.Implementation;

import com.example.dorustree_corp.Model.MongoDb.CartData;
import com.example.dorustree_corp.Repository.MongoDb.CartRepository;
import com.example.dorustree_corp.Service.Interfaces.CartService;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;


    @Autowired
    public CartServiceImplementation(CartRepository cartRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
    }


    @Override
    public void addToCart(CartData cartData) {

        String loggedInUserId = userService.findByUserId();
        Optional<CartData> cartOptional = cartRepository.findByUserId(loggedInUserId);
        String productId = cartData.getItems().keySet().iterator().next();
        Integer quantity = cartData.getItems().get(productId);

        if (cartOptional.isEmpty()) {
            CartData cart = new CartData();
            cart.setUserId(loggedInUserId);
            Map<String, Integer> cartItems = new HashMap<>();
            cartItems.put(productId, quantity);
            cart.setItems(cartItems);
            cartRepository.save(cart);
        } else {
            CartData existingCart = cartOptional.get();
            Map<String, Integer> cartItems = existingCart.getItems();
            cartItems.put(productId,
                    cartItems.getOrDefault(productId, 0) + quantity);
            existingCart.setItems(cartItems);
            cartRepository.save(existingCart);
        }
    }


    @Override
    public CartData getCart() {
        String loggedInUserId = userService.findByUserId();
        Optional<CartData> cartOptional = cartRepository.findByUserId(loggedInUserId);
        if(cartOptional.isEmpty()){
            throw new UsernameNotFoundException("no user name founder");
        } else
            return cartOptional.get();
    }

    @Override
    public void deleteCart() {
        String loggedInUserId = userService.findByUserId();
        cartRepository.deleteByUserId(loggedInUserId);
    }
}
