package com.example.dorustree_corp.Service.Interfaces;

import com.example.dorustree_corp.Model.MongoDb.CartData;

public interface CartService {
    void addToCart(CartData cartData);

    CartData getCart();

    void deleteCart();
}
