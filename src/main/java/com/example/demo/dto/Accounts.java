package com.example.demo.dto;

import com.example.demo.enums.AccountType;

public record Accounts(
    AccountType accountType,
    String accountNumber
) {}