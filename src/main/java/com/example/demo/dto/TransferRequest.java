package com.example.demo.dto;

import java.math.BigDecimal;

public record TransferRequest(    String sender,BigDecimal amount,
    String reciver,String pin) {


}