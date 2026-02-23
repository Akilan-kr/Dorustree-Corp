package com.dorustree.dorustree_corp.Dto;

import com.dorustree.dorustree_corp.Enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String email;
    private UserRoles userRole;
    private String token;
}
