package com.example.dorustree_corp.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "UserEmail cannot be null or empty")
    private String userName;
    @NotBlank(message = "Password cannot be null or empty")
    private String password;
}
