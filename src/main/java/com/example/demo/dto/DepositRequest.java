package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public record DepositRequest(	String acc,
BigDecimal amount,String pin){


}