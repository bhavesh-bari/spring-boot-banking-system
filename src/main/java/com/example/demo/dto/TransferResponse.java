package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransferResponse {

    private String message;
    private String transactionRef;
    private String senderAccount;
    private String receiverAccount;
    private BigDecimal amount;
    private Date date;
    

    public TransferResponse(
            String message,
            String transactionRef,
            String senderAccount,
            String receiverAccount,
            BigDecimal amount) {

        this.message = message;
        this.transactionRef = transactionRef;
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
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
        return senderAccount;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}