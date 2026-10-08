package com.example.demo.dto;
import java.math.BigDecimal;
public record WithdrawRequest(String acc,
BigDecimal amount,String pin){
}