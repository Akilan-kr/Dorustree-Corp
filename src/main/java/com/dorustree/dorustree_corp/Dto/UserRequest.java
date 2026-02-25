package com.dorustree.dorustree_corp.Dto;

import com.dorustree.dorustree_corp.Enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


public record UserRequest(

        @NotBlank(message = "User name cannot be empty or null")
        String userName,

        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "Email format is invalid (e.g., example@domain.com)"
        )
        @NotBlank(message = "User Email cannot be empty or null")
        String userEmail,

        @NotBlank(message = "Password cannot be empty or null")
        String userPassword,

        @NotBlank(message = "userAddress cannot be null or blank")
        String userAddress,

        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
        String userPhone
) {
}
