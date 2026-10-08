package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ResponseWD {

    private String message;
    private String transactionRef;
    private String account;
    private BigDecimal amount;
private Date date;
    public ResponseWD(
            String message,
            String transactionRef,
            String Account,
            BigDecimal amount) {
        this.message = message;
        this.transactionRef = transactionRef;
        this.account = Account;
        this.amount = amount;
        this.date=new Date();
    }
    public Date getDate() {
        return date;
    }
    public String getMessage() {
        return message;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public String getSenderAccount() {
        return account;
    }


    public BigDecimal getAmount() {
        return amount;
    }
}