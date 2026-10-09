
package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Size(max = 254, message = "Email is too long")
    String email,

    @NotBlank(message = "Phone is required")
    @Pattern(
        regexp = "^\\+?[1-9]\\d{7,14}$",
        message = "Enter a valid phone number"
    )
    String phone,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72,
          message = "Password must be 8–72 characters")
    String password
) {}