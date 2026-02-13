package com.example.dorustree_corp.Service;

import com.example.dorustree_corp.Model.MongoDb.CartData;

import java.util.List;

public interface CartService {
    void addToCart(CartData cartData);

    CartData getCart();

    void deleteCart();
}
