package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.enums.TransactionStatus;
import com.example.demo.enums.TransactionType;

public record TransactionHistoryResponse(
        Long id,
        BigDecimal amount,
        TransactionType type,
        String transactionRef,
        TransactionStatus status,
        LocalDateTime timestamp,
        String source
) {
}
