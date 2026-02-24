package com.dorustree.dorustree_corp.Dto;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        long timestamp
) {
    public ApiResponse(boolean success, String message, T data) {
        this(success, message, data, System.currentTimeMillis());
    }
}
