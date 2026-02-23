package com.dorustree.dorustree_corp.Service.Interfaces;

import com.dorustree.dorustree_corp.Model.MongoDb.CartData;

public interface ICartService {
    void addToCart(CartData cartData);

    CartData getCart();

    void deleteCart();

    void removeFromCart(CartData cartData);
}
