package com.dorustree.dorustree_corp.Dto;

import com.dorustree.dorustree_corp.Enums.UserRoles;
import com.dorustree.dorustree_corp.Enums.UserStatusForVendor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserResponse(
        String id,
        String userName,
        String userEmail,
        UserRoles userRoles,
        UserStatusForVendor userStatusForVendor,
        String userAddress,
        String userPhone,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
}
