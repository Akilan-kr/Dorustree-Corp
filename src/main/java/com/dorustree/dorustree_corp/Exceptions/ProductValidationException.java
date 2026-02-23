package com.dorustree.dorustree_corp.Exceptions;

public class ProductValidationException extends RuntimeException{
    public ProductValidationException(String message) {
        super(message);
    }
}
