package com.example.demo.entity;

import java.math.BigDecimal;

import com.example.demo.enums.AccountType;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private BigDecimal balance;
    
    private String pin;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Version
    private Long version;

    @ManyToOne
    private User user;
    public  Account() {
    	
    }
    public  Account(AccountType accountType,BigDecimal balance,User user,String pin) {
    	this.accountType=accountType;
    	this.balance=balance;
    	this.user=user;
    	this.pin=pin;
    }


    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Long getVersion() {
        return version;
    }

    public User getUser() {
        return user;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getPin() {
		return pin;
	}public void setPin(String pin) {
		this.pin = pin;
	}
}