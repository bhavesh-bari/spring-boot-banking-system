
package com.example.demo.dto;

import java.math.BigDecimal;

import com.example.demo.enums.AccountType;

public record CreateAccountRequest(
        BigDecimal balance,
        AccountType accountType,
        Long userId,
        String pin
) {
}
