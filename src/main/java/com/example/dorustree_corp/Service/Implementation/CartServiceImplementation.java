package com.example.dorustree_corp.Service.Implementation;

import com.example.dorustree_corp.Model.MongoDb.CartData;
import com.example.dorustree_corp.Repository.MongoDb.CartRepository;
import com.example.dorustree_corp.Service.Interfaces.CartService;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
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
        if (cartData.getItems() == null || cartData.getItems().isEmpty()) {
            log.warn("Attempted to add to cart with empty items for user {}", userService.findByUserId());
            return;
        }

        String loggedInUserId = userService.findByUserId();
        Optional<CartData> cartOptional = cartRepository.findByUserId(loggedInUserId);

        String productId = cartData.getItems().keySet().iterator().next();
        Integer quantity = cartData.getItems().get(productId);

        if (cartOptional.isEmpty()) {
            CartData cart = new CartData();
            cart.setUserId(loggedInUserId);
            cart.setItems(new HashMap<>(cartData.getItems()));
            log.info("S: User({}) created a new cart", loggedInUserId);
            cartRepository.save(cart);
        } else {
            CartData existingCart = cartOptional.get();
            Map<String, Integer> cartItems = existingCart.getItems();
            cartItems.put(productId, cartItems.getOrDefault(productId, 0) + quantity);
            existingCart.setItems(cartItems);
            log.info("S: User({}) updated the cart with new product", loggedInUserId);
            cartRepository.save(existingCart);
        }
    }




    @Override
    public void removeFromCart(CartData cartData) {
        String loggedInUserId = userService.findByUserId();
        Optional<CartData> cartOptional = cartRepository.findByUserId(loggedInUserId);

        if (cartOptional.isEmpty()) {
            log.warn("S: User({}) tried to remove item but cart does not exist", loggedInUserId);
            return;
        }

        CartData existingCart = cartOptional.get();

        String productId = cartData.getItems().keySet().iterator().next();
        Integer quantityToRemove = cartData.getItems().get(productId);

        Map<String, Integer> cartItems = existingCart.getItems();

        if (!cartItems.containsKey(productId)) {
            log.warn("S: Product({}) not found in User({}) cart", productId, loggedInUserId);
            return;
        }

        int currentQuantity = cartItems.get(productId);

        if (currentQuantity <= quantityToRemove) {
            // remove product completely
            cartItems.remove(productId);
            log.info("S: Product({}) removed completely from User({}) cart",
                    productId, loggedInUserId);
        } else {
            // decrease quantity
            cartItems.put(productId, currentQuantity - quantityToRemove);
            log.info("S: Product({}) quantity decreased for User({})",
                    productId, loggedInUserId);
        }
        existingCart.setItems(cartItems);
        cartRepository.save(existingCart);
    }



    @Override
    public CartData getCart() {
        String loggedInUserId = userService.findByUserId();
        Optional<CartData> cartOptional = cartRepository.findByUserId(loggedInUserId);
        if(cartOptional.isEmpty()){
            log.error("S: No cart founded for a user({})", loggedInUserId);
            throw new UsernameNotFoundException("No Cart with login user name is founded");
        } else {
            log.info("S: Getting cart for the user({})", loggedInUserId);
            return cartOptional.get();
        }
    }

    @Override
    public void deleteCart() {
        String loggedInUserId = userService.findByUserId();
        log.warn("S: The whole cart is deleted by the user({})", loggedInUserId);
        cartRepository.deleteByUserId(loggedInUserId);
    }


}
