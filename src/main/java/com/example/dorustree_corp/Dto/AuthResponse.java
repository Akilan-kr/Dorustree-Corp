package com.example.dorustree_corp.Dto;

import com.example.dorustree_corp.Enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String email;
    private UserRoles userRoles;
    private String token;
}
